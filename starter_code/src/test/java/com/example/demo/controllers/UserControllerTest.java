package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void init() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void test_createUser_success() {
        when(bCryptPasswordEncoder.encode("password")).thenReturn("hashedPassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setConfirmPassword("password");

        ResponseEntity<User> response =  userController.createUser(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    public void test_createUser_return_bad_request() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("username");
        request.setPassword("pass");
        request.setConfirmPassword("pass");

        ResponseEntity<User> response =  userController.createUser(request);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void test_findByUserName_success() {
        when(userRepository.findByUsername(anyString())).thenReturn(createUser());
        ResponseEntity<User> response = userController.findByUserName("Test");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("Test", user.getUsername());
    }

    @Test
    public void test_findByUserName_return_not_found() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        ResponseEntity<User> response = userController.findByUserName("Test");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_findById_success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(createUser()));
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("Test", user.getUsername());
    }

    @Test
    public void test_findById_return_not_found() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("Test");
        return user;
    }
}
