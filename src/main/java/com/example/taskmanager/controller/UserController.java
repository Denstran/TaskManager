package com.example.taskmanager.controller;

import com.example.taskmanager.model.User;
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
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/users/update")
    public ResponseEntity<User> update(@Valid @RequestBody User user, @RequestParam(value = "usrId") Long usrId,
                                       @AuthenticationPrincipal UserDetailsImpl authUser) {
        User userResult = userService.updateUser(user, usrId, authUser);

        return new ResponseEntity<>(userResult, HttpStatus.OK);
    }

    @GetMapping("/users/user")
    public ResponseEntity<User> getOneUser(@RequestParam(value = "usrId") Long usrId,
                                           @AuthenticationPrincipal UserDetailsImpl authUser) {
        User user = userService.findByUserId(usrId, authUser);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam(value = "usrId") Long usrId,
                                             @AuthenticationPrincipal UserDetailsImpl authUser) {
        userService.deleteById(usrId, authUser);

        return new ResponseEntity<>("User was deleted", HttpStatus.NO_CONTENT);
    }
}
