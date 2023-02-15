package com.example.taskmanager.model;

import com.example.taskmanager.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "task")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task")
    @NotBlank(message = "Task text must not be blank")
    @Size(max = 255, message = "Task text must not me greater then 255 characters.")
    private String task;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",
            columnDefinition = "varchar(32) default 'IN_PROGRESS'")
    private Status status = Status.IN_PROGRESS;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usr_id", nullable = false)
    @JsonIgnore
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
