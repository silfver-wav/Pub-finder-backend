package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.LoginRequest;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserDTO registerRequest) {
        return userService.registerUser(Mapper.INSTANCE.dtoToEntity(registerRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/edit")
    public ResponseEntity<UserDTO> editUser(@RequestBody UserDTO userDTO) {
        return userService.editUser(Mapper.INSTANCE.dtoToEntity(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}
