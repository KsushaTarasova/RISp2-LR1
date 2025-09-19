package by.bsuir.lr1.service;

import by.bsuir.lr1.dto.TaskCreateRequest;
import by.bsuir.lr1.dto.TaskResponse;
import by.bsuir.lr1.entity.TaskStatus;
import by.bsuir.lr1.dto.TaskUpdateRequest;

import java.util.List;

public interface TaskService {

    TaskResponse create(TaskCreateRequest request);

    TaskResponse getById(Long id);

    List<TaskResponse> getAll(TaskStatus statusFilter);

    TaskResponse update(Long id, TaskUpdateRequest request);

    void delete(Long id);
}
