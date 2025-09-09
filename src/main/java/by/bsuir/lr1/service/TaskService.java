package by.bsuir.lr1;

import java.util.List;

public interface TaskService {

    TaskResponse create(TaskCreateRequest request);

    TaskResponse getById(Long id);

    List<TaskResponse> getAll(TaskStatus statusFilter);

    TaskResponse update(Long id, TaskUpdateRequest request);

    void delete(Long id);
}
