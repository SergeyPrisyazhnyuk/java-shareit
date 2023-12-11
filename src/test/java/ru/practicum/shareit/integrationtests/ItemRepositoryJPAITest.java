package ru.practicum.shareit.integrationtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.ItemRepository;

@DataJpaTest
class ItemRepositoryJPAITest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void search() {
    }

    @Test
    void findAllByOwnerId() {
    }
}