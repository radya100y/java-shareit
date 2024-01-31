package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemIntegrityTest {

    private final ItemService service;
    private final UserService userService;
    private final EntityManager em;

    @Test
    void getAllSaved() {
        UserDto user = UserDto.builder().email("rty@rty.rty").name("rty").build();
        UserDto savedUser = userService.save(user);

        ItemDto item1 = ItemDto.builder().name("qwe").description("qwe").available(true).owner(savedUser.getId())
                .build();
        service.save(item1, savedUser.getId());

        ItemDto item2 = ItemDto.builder().name("wer").description("wer").available(true).owner(savedUser.getId())
                .build();
        service.save(item2, savedUser.getId());

        TypedQuery<Item> query = em.createQuery("select i from Item i where owner = :userId", Item.class);
        List<Item> items = query.setParameter("userId", savedUser.getId()).getResultList();

        Assertions.assertEquals(items.size(), 2);
        Assertions.assertEquals(items.get(0).getAvailable(), true);
    }
}
