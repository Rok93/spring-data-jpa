package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void save() {
        //given
        Item item = new Item("A");

        //when
        itemRepository.save(item);

        //then
    }
}
