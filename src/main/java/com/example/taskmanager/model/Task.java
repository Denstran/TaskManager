package com.example.taskmanager.model;

import com.example.taskmanager.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private String task;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",
            columnDefinition = "varchar(32) default 'IN_PROGRESS'")
    private Status status = Status.IN_PROGRESS;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usr_id", nullable = false)
    private User user;
}
