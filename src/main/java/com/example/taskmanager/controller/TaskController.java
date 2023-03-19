package com.example.taskmanager.controller;

import com.example.taskmanager.model.Status;
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

import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getUserTasks(@RequestParam(value = "usrId") Long usrId,
                                                   @AuthenticationPrincipal UserDetailsImpl authUser,
                                                   @RequestParam(defaultValue = "0", value = "page") int page,
                                                   @RequestParam(defaultValue = "3", value = "size") int size,
                                                   @RequestParam(required = false, value = "status") Status status) {
        Map<String, Object> response = taskService.getTasks(usrId, authUser, page, size, status);

        return new ResponseEntity<>(response, HttpStatus.OK);
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
