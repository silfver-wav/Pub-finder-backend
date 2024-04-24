package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.LoginRequest;
import com.pubfinder.pubfinder.dto.UVPDTO;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserDTO registerRequest) throws BadRequestException {
        AuthenticationResponse response = userService.registerUser(Mapper.INSTANCE.dtoToEntity(registerRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserDTO user, HttpServletRequest request) throws ResourceNotFoundException {
        userService.deleteUser(Mapper.INSTANCE.dtoToEntity(user), request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit")
    public ResponseEntity<UserDTO> editUser(@RequestBody UserDTO userDTO, HttpServletRequest request) throws BadRequestException, ResourceNotFoundException {
        UserDTO editedUser = userService.editUser(Mapper.INSTANCE.dtoToEntity(userDTO), request);
        return ResponseEntity.ok().body(editedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) throws Exception {
        return ResponseEntity.ok(userService.refreshToken(request));
    }

    @PostMapping("/revokeUserAccess")
    public ResponseEntity<Void> revokeUserAccess(@PathVariable String email) {
        userService.revokeUserAccess(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getVisitedPubs/{username}")
    public ResponseEntity<List<UVPDTO>> getVisitedPubs(@PathVariable String username) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.getVisitedPubs(username));
    }
}