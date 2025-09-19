package by.bsuir.lr1.dto;

import by.bsuir.lr1.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskCreateRequest {
    @NotBlank(message = "title не должен быть пустым")
    @Size(max = 200, message = "title слишком длинный")
    private String title;
    @Size(max = 2000, message = "description слишком длинное")
    private String description;
    private TaskStatus status = TaskStatus.NEW;
    public TaskCreateRequest() {
    }
}
