package by.bsuir.lr1;

import by.bsuir.lr1.dto.TaskCreateRequest;
import by.bsuir.lr1.dto.TaskCreatedEvent;
import by.bsuir.lr1.dto.TaskResponse;
import by.bsuir.lr1.dto.TaskUpdateRequest;
import by.bsuir.lr1.entity.Task;
import by.bsuir.lr1.entity.TaskStatus;
import by.bsuir.lr1.exception.ResourceNotFoundException;
import by.bsuir.lr1.repository.TaskRepository;
import by.bsuir.lr1.service.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    TaskRepository repository;
    @Mock
    ApplicationEventPublisher publisher;
    @InjectMocks
    TaskServiceImpl service;

    @Test
    void create_publishesEvent_andReturnsResponse() {
        // Arrange
        TaskCreateRequest req = new TaskCreateRequest();
        req.setTitle("Test");
        req.setDescription("Desc");
        req.setStatus(TaskStatus.NEW);

        Task saved = new Task("Test", "Desc", TaskStatus.NEW);
        saved.setId(1L);

        when(repository.save(any(Task.class))).thenReturn(saved);

        // Act
        TaskResponse resp = service.create(req);

        // Assert
        assertNotNull(resp.getId());
        assertEquals("Test", resp.getTitle());
        verify(repository, times(1)).save(any(Task.class));
        // Проверяем, что событие опубликовано
        verify(publisher, times(1)).publishEvent(any(TaskCreatedEvent.class));
    }

    @Test
    void getById_found_returnsResponse() {
        Task task = new Task("A", "B", TaskStatus.NEW);
        task.setId(10L);
        when(repository.findById(10L)).thenReturn(Optional.of(task));

        TaskResponse resp = service.getById(10L);

        assertEquals(10L, resp.getId());
        verify(repository).findById(10L);
    }

    @Test
    void getById_notFound_throws() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void getAll_withFilter() {
        when(repository.findByStatus(TaskStatus.DONE))
                .thenReturn(List.of(new Task("T", "D", TaskStatus.DONE)));

        List<TaskResponse> list = service.getAll(TaskStatus.DONE);
        assertEquals(1, list.size());
        assertEquals(TaskStatus.DONE, list.get(0).getStatus());
    }

    @Test
    void update_partial() {
        Task task = new Task("Old", "Old", TaskStatus.NEW);
        task.setId(5L);
        when(repository.findById(5L)).thenReturn(Optional.of(task));
        when(repository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskUpdateRequest req = new TaskUpdateRequest();
        req.setStatus(TaskStatus.IN_PROGRESS);

        TaskResponse resp = service.update(5L, req);

        assertEquals(TaskStatus.IN_PROGRESS, resp.getStatus());
        verify(repository).save(any(Task.class));
    }

    @Test
    void delete_ok() {
        when(repository.existsById(7L)).thenReturn(true);
        service.delete(7L);
        verify(repository).deleteById(7L);
    }

    @Test
    void delete_notFound() {
        when(repository.existsById(8L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.delete(8L));
        verify(repository, never()).deleteById(anyLong());
    }
}
