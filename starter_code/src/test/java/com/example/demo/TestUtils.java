package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
                wasPrivate = true;
            }
            field.set(target, toInject);
            if (wasPrivate) {
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Test");
        user.setCart(new Cart());
        return user;
    }

    public static Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setDescription("Item's description");
        item.setName("Item's name");
        item.setPrice(new BigDecimal(22));
        return item;
    }
}
