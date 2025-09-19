package by.bsuir.lr1.repository;

import by.bsuir.lr1.entity.Task;
import by.bsuir.lr1.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
}
