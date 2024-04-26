package com.pubfinder.pubfinder.controller;

import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.LoginRequest;
import com.pubfinder.pubfinder.dto.ReviewDto;
import com.pubfinder.pubfinder.dto.VisitedDto;
import com.pubfinder.pubfinder.dto.UserDto;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserDto registerRequest)
      throws BadRequestException {
    AuthenticationResponse response = userService.registerUser(
        Mapper.INSTANCE.dtoToEntity(registerRequest));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> delete(@RequestBody UserDto user, HttpServletRequest request)
      throws ResourceNotFoundException {
    userService.delete(Mapper.INSTANCE.dtoToEntity(user), request);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/edit")
  public ResponseEntity<UserDto> edit(@RequestBody UserDto userDTO, HttpServletRequest request)
      throws BadRequestException, ResourceNotFoundException {
    return ResponseEntity.ok()
        .body(userService.edit(Mapper.INSTANCE.dtoToEntity(userDTO), request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest)
      throws ResourceNotFoundException {
    return ResponseEntity.ok(userService.login(loginRequest));
  }

  @PostMapping("/refreshToken")
  public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request)
      throws Exception {
    return ResponseEntity.ok(userService.refreshToken(request));
  }

  @PostMapping("/revokeUserAccess")
  public ResponseEntity<Void> revokeUserAccess(@PathVariable String email) {
    userService.revokeUserAccess(email);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/getVisitedPubs/{username}")
  public ResponseEntity<List<VisitedDto>> getVisitedPubs(@PathVariable String username)
      throws ResourceNotFoundException {
    return ResponseEntity.ok(userService.getVisitedPubs(username));
  }

  @GetMapping("/reviews/{username}")
  public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable("username") String username)
      throws ResourceNotFoundException {
    return ResponseEntity.ok(userService.getUserReviews(username));
  }
}