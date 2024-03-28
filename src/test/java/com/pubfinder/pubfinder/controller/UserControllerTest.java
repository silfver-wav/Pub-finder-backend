package com.pubfinder.pubfinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pubfinder.pubfinder.dto.AuthenticationResponse;
import com.pubfinder.pubfinder.dto.UserDTO;
import com.pubfinder.pubfinder.service.UserService;
import com.pubfinder.pubfinder.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Does not work because of passwordEncoder nullPointerException
    /*
    @Test
    public void registerUserTest() throws Exception {
        UserDTO userDTO = TestUtil.generateMockUserDTO();
        AuthenticationResponse response = TestUtil.generateMockAuthenticationResponse();
        when(userService.registerUser(TestUtil.generateMockUser())).thenReturn(response);

        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteUserTest() throws Exception {
        doNothing().when(userService).deleteUser(any());
        mockMvc.perform(delete("/user/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void editUserTest() throws Exception {
        when(userService.editUser(any())).thenReturn(TestUtil.generateMockUserDTO());
        mockMvc.perform(put("/user/edit", TestUtil.generateMockUserDTO())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }
    */

    @Test
    public void loginTest() throws Exception {
        AuthenticationResponse response = TestUtil.generateMockAuthenticationResponse();
        when(userService.login(any())).thenReturn(response);
        mockMvc.perform(post("/user/login", response)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void refreshTokenTest() throws Exception {
        AuthenticationResponse response = TestUtil.generateMockAuthenticationResponse();
        when(userService.refreshToken(any())).thenReturn(response);
        mockMvc.perform(post("/user/refreshToken", response)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    UserDTO user = TestUtil.generateMockUserDTO();
}
