package com.example.taskmanager.service;

import com.example.taskmanager.exceptions.ResourceNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task, User user) {
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Task task, Long taskId) {
        Task _task = taskRepository.findById(taskId).orElseThrow(() ->
                new ResourceNotFoundException("Not found task with id: " + taskId));

        if (task.getStatus() != null) {
            _task.setStatus(task.getStatus());
        }

        System.out.println(task.getTask());
        if (task.getTask() != null) {
            _task.setTask(task.getTask());
        }
        return taskRepository.save(_task);
    }

    public List<Task> getTasks(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Not found user with id: " + userId));
        return taskRepository.findByUserId(userId);
    }

    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() ->
                new ResourceNotFoundException("Not found task with id: " + taskId));
    }

    public void deleteTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new ResourceNotFoundException("Not found task with id: " + taskId));
        taskRepository.delete(task);
    }
}
