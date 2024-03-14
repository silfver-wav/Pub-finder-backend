package com.pubfinder.pubfinder.repository;

import com.pubfinder.pubfinder.db.UserRepository;
import com.pubfinder.pubfinder.models.Role;
import com.pubfinder.pubfinder.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUserTest() {
        User user = new User("username", "fistname", "lastname", "email@email.com", "password");
        user.setRole(Role.USER);
        User result = userRepository.save(user);

        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getRole(), result.getRole());
    }

    @Test
    public void getUserByIdTest() {
        User user = new User("username", "fistname", "lastname", "email@email.com", "password");
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        Optional<User> result = userRepository.findById(savedUser.getId());

        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
        assertEquals(user.getFirstName(), result.get().getFirstName());
        assertEquals(user.getLastName(), result.get().getLastName());
        assertEquals(user.getEmail(), result.get().getEmail());
        assertEquals(user.getPassword(), result.get().getPassword());
        assertEquals(user.getRole(), result.get().getRole());
    }

    @Test
    public void deleteUserTest() {
        User user = new User("username", "fistname", "lastname", "email@email.com", "password");
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        userRepository.delete(savedUser);

        Optional<User> result = userRepository.findById(savedUser.getId());

        assertTrue(result.isEmpty());
    }
}
