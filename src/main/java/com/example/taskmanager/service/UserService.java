package com.example.taskmanager.service;

import com.example.taskmanager.exceptions.ResourceNotFoundException;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Not found User with id: " + userId));
    }

    public boolean saveUser(User userForSave) {
        User userFromDb = userRepository.findByUsername(userForSave.getUsername());

        if (userFromDb == null) {
            userRepository.save(userForSave);
            return true;
        }else if (userFromDb.getId() == userForSave.getId()) { // Updating user.
            userRepository.save(userForSave);
            return true;
        }

        throw new ResourceNotFoundException("User with username: " + userForSave.getUsername() + " already exists");
    }

    public void deleteById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Not found User with id: " + userId));
        userRepository.delete(user);
    }
}
