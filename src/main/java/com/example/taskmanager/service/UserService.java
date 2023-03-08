package com.example.taskmanager.service;

import com.example.taskmanager.exceptions.AuthenticationException;
import com.example.taskmanager.exceptions.ResourceAlreadyExistsException;
import com.example.taskmanager.exceptions.ResourceNotFoundException;
import com.example.taskmanager.model.ERole;
import com.example.taskmanager.model.Role;
import com.example.taskmanager.model.User;
import com.example.taskmanager.payload.request.LoginRequest;
import com.example.taskmanager.payload.request.SignupRequest;
import com.example.taskmanager.payload.response.JwtResponse;
import com.example.taskmanager.repository.RoleRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.service.security.jwt.JwtUtils;
import com.example.taskmanager.service.security.securityservice.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder,
                       JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    public User findByUserId(Long userId, UserDetailsImpl authUser) {
        if (userId != authUser.getId()){
            throw new AuthenticationException("Access denied!");
        }

        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Not found User with id: " + userId));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())){
            throw new ResourceAlreadyExistsException("User with username: " + signupRequest.getUsername() +
                    " already exists");
        }

        if (userRepository.existsByMail(signupRequest.getMail())){
            throw new ResourceAlreadyExistsException("User with email: " + signupRequest.getMail() +
                    " already exists");
        }

        User user = new User(signupRequest.getUsername(),
                             encoder.encode(signupRequest.getPassword()),
                             signupRequest.getMail());
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() ->
                new ResourceNotFoundException("Not found role with name: " + ERole.ROLE_USER));
        user.getRoles().add(userRole);

        userRepository.save(user);
    }

    public User updateUser(User userForUpdate, Long usrId, UserDetailsImpl authUser) {
        if (usrId != authUser.getId()){
            throw new AuthenticationException("Access denied!");
        }

        User _user = userRepository.findById(usrId).orElseThrow(() ->
                new ResourceNotFoundException("Not found user with id: " + usrId));

        Optional<User> userFromDb = userRepository.findByUsername(userForUpdate.getUsername());

        if (userFromDb.isPresent() && !userFromDb.get().equals(_user)) {
            throw new ResourceAlreadyExistsException("User with username: " + userForUpdate.getUsername() +
                                                        " already exists");
        }

        _user.setUsername(userForUpdate.getUsername());
        _user.setMail(userForUpdate.getMail());
        _user.setPassword(userForUpdate.getPassword());

        return userRepository.save(_user);
    }

    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getMail(),
                roles);
    }

    public void deleteById(Long userId, UserDetailsImpl authUser) {
        if (userId != authUser.getId()){
            throw new AuthenticationException("Access denied!");
        }
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Not found User with id: " + userId));
        userRepository.delete(user);
    }
}
