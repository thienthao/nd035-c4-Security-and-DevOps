package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.TestUtils.createItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void init() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void test_getItem_success() {
        Item existedItem = createItem();
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(existedItem));
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Item> items = response.getBody();
        assertNotNull(items);
        Item responseItem = items.get(0);
        assertEquals(1L, responseItem.getId().longValue());
        assertEquals("Item's description", responseItem.getDescription());
        assertEquals("Item's name", responseItem.getName());
        assertEquals(22, responseItem.getPrice().intValue());
    }

    @Test
    public void test_getItemById_success() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(createItem()));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals(1L, item.getId().longValue());
        assertEquals("Item's description", item.getDescription());
        assertEquals("Item's name", item.getName());
        assertEquals(22, item.getPrice().intValue());
    }

    @Test
    public void test_getItemById_return_not_found() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test_getItemsByName_success() {
        Item existedItem = createItem();
        when(itemRepository.findByName(anyString())).thenReturn(Collections.singletonList(existedItem));
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item's name");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Item> items = response.getBody();
        assertNotNull(items);
        Item responseItem = items.get(0);
        assertEquals(1L, responseItem.getId().longValue());
        assertEquals("Item's description", responseItem.getDescription());
        assertEquals("Item's name", responseItem.getName());
        assertEquals(22, responseItem.getPrice().intValue());
    }

    @Test
    public void test_getItemsByName_return_not_found() {
        when(itemRepository.findByName(anyString())).thenReturn(Collections.emptyList());
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item's name");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
