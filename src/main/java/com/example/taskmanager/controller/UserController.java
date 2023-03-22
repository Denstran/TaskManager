package com.example.taskmanager.controller;

import com.example.taskmanager.model.User;
import com.example.taskmanager.payload.response.StatisticResponse;
import com.example.taskmanager.service.UserService;
import com.example.taskmanager.service.security.securityservice.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(userService.findAll(page, size), HttpStatus.OK);
    }

    @GetMapping("/users/user/getStatistic")
    public ResponseEntity<StatisticResponse> getUserStatistic(@AuthenticationPrincipal UserDetailsImpl authUser) {

        return new ResponseEntity<>(userService.getUserStatistic(authUser), HttpStatus.OK);
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
