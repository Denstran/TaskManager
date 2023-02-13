package com.example.taskmanager.service;

import com.example.taskmanager.exceptions.ResourceAlreadyExistsException;
import com.example.taskmanager.exceptions.ResourceNotFoundException;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public User findByUsername(String username) {
        User userFromDb = userRepository.findByUsername(username);

        if (userFromDb == null)
            throw new ResourceNotFoundException("Not found user with username: " + username);

        return userFromDb;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User saveUser(User userForSave) {
        User userFromDb = userRepository.findByUsername(userForSave.getUsername());

        if (userFromDb == null) {
            return userRepository.save(userForSave);
        }

        throw new ResourceNotFoundException("User with username: " + userForSave.getUsername() + " already exists");
    }

    public User updateUser(User userForUpdate, Long usrId) {
        User _user = userRepository.findById(usrId).orElseThrow(() ->
                new ResourceNotFoundException("Not found user with id: " + usrId));

        User userFromDb = userRepository.findByUsername(userForUpdate.getUsername());

        if (userFromDb != null && !userFromDb.equals(_user)) {
            throw new ResourceAlreadyExistsException("User with username: " + userForUpdate.getUsername() +
                                                        " already exists");
        }

        _user.setUsername(userForUpdate.getUsername());
        _user.setMail(userForUpdate.getMail());
        _user.setPassword(userForUpdate.getPassword());

        return userRepository.save(_user);
    }

    public void deleteById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Not found User with id: " + userId));
        userRepository.delete(user);
    }
}
