package com.example.taskmanager.controller;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.service.TaskService;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.service.security.securityservice.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @PostMapping("/users/user/createTask")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task, @RequestParam(value = "usrId") Long usrId,
                                           @AuthenticationPrincipal UserDetailsImpl authUser) {
        User user = userService.findByUserId(usrId, authUser);
        Task _task = taskService.createTask(task, user);

        return new ResponseEntity<>(_task, HttpStatus.CREATED);
    }

    @PutMapping("/users/user/tasks/task/updateTask")
    public ResponseEntity<Task> updateTask(@RequestBody Task task,
                                           @RequestParam(value = "taskId") Long taskId,
                                           @AuthenticationPrincipal UserDetailsImpl authUser) {
        Task _task = taskService.updateTask(task, taskId, authUser);

        return new ResponseEntity<>(_task, HttpStatus.OK);
    }

    @GetMapping("/users/user/tasks")
    public ResponseEntity<List<Task>> getUserTasks(@RequestParam(value = "usrId") Long usrId,
                                                   @AuthenticationPrincipal UserDetailsImpl authUser) {
        List<Task> tasks = taskService.getTasks(usrId, authUser);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/users/user/tasks/task")
    public ResponseEntity<Task> getOneTask(@RequestParam(value = "taskId") Long taskId,
                                           @AuthenticationPrincipal UserDetailsImpl authUser) {
        Task task = taskService.findTaskById(taskId, authUser);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/users/user/tasks/task")
    public ResponseEntity<String> deleteTask(@RequestParam(value = "taskId") Long taskId,
                                             @AuthenticationPrincipal UserDetailsImpl authUser){
        taskService.deleteTaskById(taskId, authUser);

        return new ResponseEntity<>("Task deleted", HttpStatus.OK);
    }


}
