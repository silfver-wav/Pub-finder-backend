package com.pubfinder.pubfinder.service;

import com.pubfinder.pubfinder.db.TokenRepository;
import com.pubfinder.pubfinder.db.UserRepository;
import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.LoginRequest;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.exception.ResourceNotFoundException;
import com.pubfinder.pubfinder.mapper.Mapper;
import com.pubfinder.pubfinder.models.Pub;
import com.pubfinder.pubfinder.models.Token;
import com.pubfinder.pubfinder.models.User;
import com.pubfinder.pubfinder.security.AuthenticationService;
import com.pubfinder.pubfinder.util.TestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "spring.cache.type=none",
        "bucket4j.enabled=false",
        "spring.datasource.url=",
        "spring.jpa.database-platform=",
        "spring.jpa.hibernate.ddl-auto=none"
})
public class UserAndAuthenticationServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void registerUserTest() throws BadRequestException {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(tokenRepository.save(any())).thenReturn(new Token());

        AuthenticationResponse result = userService.registerUser(user);
        assertTrue(result.getAccessToken() != null && result.getRefreshToken() != null);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void registerUserTestBadRequest() throws BadRequestException {
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        assertThrows( BadRequestException.class, () -> userService.registerUser(user));
    }

    @Test
    public void deleteUserTest() throws ResourceNotFoundException {
        doNothing().when(userRepository).delete(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(tokenRepository.findAllTokensByUser(user.getId())).thenReturn(List.of(token));
        doNothing().when(tokenRepository).delete(token);
        userService.deleteUser(user.getId());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).delete(any());
        verify(tokenRepository, times(1)).findAllTokensByUser(any());
        verify(tokenRepository, times(1)).delete(any());
    }

    @Test
    public void deleteUserTestResourceNotFound() throws ResourceNotFoundException {
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(user.getId()));
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    public void editUserTest() throws BadRequestException, ResourceNotFoundException {
        User editedUser = TestUtil.generateUser();
        editedUser.setUsername("Something else");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(editedUser);

        when(tokenRepository.findAllTokensByUser(user.getId())).thenReturn(List.of(token));
        doNothing().when(tokenRepository).delete(token);

        UserDTO result = userService.editUser(editedUser);
        assertEquals(Mapper.INSTANCE.entityToDto(editedUser), result);
        verify(userRepository, times(1)).save(editedUser);
    }

    @Test
    public void editUserTestBadRequest() {
        assertThrows(BadRequestException.class, () -> userService.editUser(user));
    }

    @Test
    public void editUserTestResourceNotFound() {
        User editedUser = TestUtil.generateUser();
        editedUser.setUsername("Something else");
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.editUser(editedUser));
    }

    @Test
    public void loginTest() throws ResourceNotFoundException {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));
        when(tokenRepository.findAllTokensByUser(user.getId())).thenReturn(List.of(token));
        doNothing().when(tokenRepository).delete(token);
        when(tokenRepository.save(token)).thenReturn(token);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        LoginRequest loginRequest = LoginRequest.builder().email("email").password("password").build();
        AuthenticationResponse response = userService.login(loginRequest);

        assert(response.getAccessToken() != null && response.getRefreshToken() != null);
    }

    @Test
    public void loginTestResourceNotFound() {
        LoginRequest loginRequest = LoginRequest.builder().email("email").password("password").build();
        assertThrows(ResourceNotFoundException.class, () -> userService.login(loginRequest));
    }

    @Test
    public void refreshTokenTestBadRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        assertThrows(BadRequestException.class, () -> userService.refreshToken(request));
    }


    private final User user = TestUtil.generateUser();
    private final Token token = TestUtil.generateToken(user);
}
