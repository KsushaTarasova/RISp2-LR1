package by.bsuir.lr1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private TaskStatus status = TaskStatus.NEW;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    public Task() {
    }

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status == null ? TaskStatus.NEW : status;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = Instant.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }
}
