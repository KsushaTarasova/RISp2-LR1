package by.bsuir.lr1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskEventListener {
    @EventListener
    public void onTaskCreated(TaskCreatedEvent event) {
        log.info("Task created event received: id={}, title={}",
                event.task().getId(), event.task().getTitle());
    }
}
