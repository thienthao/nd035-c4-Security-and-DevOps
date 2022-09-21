package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void init() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void test_submit_success() {
        User user = createUser();
        Item item = createItem();
        Cart userCart = user.getCart();
        userCart.setId(1L);
        userCart.setItems(new ArrayList<>());
        userCart.getItems().add(item);
        userCart.setTotal(BigDecimal.valueOf(22));
        userCart.setUser(user);
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("Test");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(user, order.getUser());
        assertEquals(item, order.getItems().get(0));
        assertEquals(22, order.getTotal().intValue());
    }

    @Test
    public void test_submit_return_not_found_when_user_is_null() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("Test");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_getOrdersForUser_success() {
        User user = createUser();
        when(userRepository.findByUsername(anyString())).thenReturn(user);

        UserOrder order = new UserOrder();
        order.setUser(user);
        when(orderRepository.findByUser(any(User.class))).thenReturn(Collections.singletonList(order));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Test");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
        assertEquals(user, order.getUser());
    }

    @Test
    public void test_getOrdersForUser_return_not_found_when_user_is_null() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Test");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Test");
        user.setCart(new Cart());
        return user;
    }

    private Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setDescription("Item's description");
        item.setName("Item's name");
        item.setPrice(new BigDecimal(22));
        return item;
    }
}
