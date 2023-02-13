package com.example.taskmanager.controller;

import com.example.taskmanager.model.User;
import com.example.taskmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User _user = userService.saveUser(user);

        return new ResponseEntity<>(_user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<User> update(@Valid @RequestBody User user, @RequestParam(value = "usrId") Long usrId) {
        User userResult = userService.updateUser(user, usrId);

        return new ResponseEntity<>(userResult, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getOneUser(@RequestParam(value = "usrId") Long usrId) {
        User user = userService.findByUserId(usrId);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
