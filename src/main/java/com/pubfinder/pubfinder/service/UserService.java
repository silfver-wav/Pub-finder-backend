package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.UserRepository;
import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.LoginRequest;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Role;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<AuthenticationResponse> registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            return ResponseEntity.badRequest().build();

        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setRole(Role.ADMIN);

        userRepository.save(user);
        var jwtToken = authenticationService.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthenticationResponse(jwtToken));
    }

    public ResponseEntity<String> deleteUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        userRepository.delete(user.get());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<UserDTO> editUser(User user) {
        if (user.getId() == null) return ResponseEntity.badRequest().build();

        Optional<User> foundUser = userRepository.findById(user.getId());
        if (foundUser.isEmpty()) return ResponseEntity.notFound().build();

        if (user.getPassword() != null) {
            String password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);
        }
        user.setRole(Role.USER);
        // TODO ta bort jwtToken och gör en ny
        User editedUser = userRepository.save(user);
        return ResponseEntity.ok().body(Mapper.INSTANCE.entityToDto(editedUser));
    }

    public ResponseEntity<AuthenticationResponse> login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow();

        var jwtToken = authenticationService.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthenticationResponse(jwtToken));
    }

}
