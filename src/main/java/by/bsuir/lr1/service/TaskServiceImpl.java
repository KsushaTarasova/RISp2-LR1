package by.bsuir.lr1;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public TaskServiceImpl(TaskRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public TaskResponse create(TaskCreateRequest request) {
        Task entity = new Task(request.getTitle(), request.getDescription(), request.getStatus());
        entity = repository.save(entity);

        TaskResponse response = toResponse(entity);

        eventPublisher.publishEvent(new TaskCreatedEvent(response));

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена: id=" + id));
        return toResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAll(TaskStatus statusFilter) {
        List<Task> tasks = (statusFilter == null)
                ? repository.findAll()
                : repository.findByStatus(statusFilter);
        return tasks.stream().map(this::toResponse).toList();
    }

    @Override
    public TaskResponse update(Long id, TaskUpdateRequest request) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Задача не найдена: id=" + id));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());

        Task saved = repository.save(task);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Задача не найдена: id=" + id);
        }
        repository.deleteById(id);
    }

    private TaskResponse toResponse(Task t) {
        return new TaskResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }
}
