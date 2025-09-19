package by.bsuir.lr1.dto;

import by.bsuir.lr1.entity.TaskStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskUpdateRequest {
    @Size(max = 200, message = "title слишком длинный")
    private String title;
    @Size(max = 2000, message = "description слишком длинное")
    private String description;
    private TaskStatus status;
    public TaskUpdateRequest() {
    }
}
