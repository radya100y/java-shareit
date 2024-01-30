package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Transactional
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestIntegrityTest {

    private final ItemRequestService service;
    private final UserService userService;
    private final EntityManager em;


    @Test
    void save() {

        ItemRequestDtoIn irIn = new ItemRequestDtoIn(1L, "qwe");
        UserDto user = UserDto.builder().id(1L).email("qwe@qwe.qwe").name("qwe").build();

        userService.save(user);
        service.save(irIn);

        TypedQuery<ItemRequest> query = em.createQuery(
                "Select r from ItemRequest r where r.description = :description", ItemRequest.class);
        ItemRequest ir = query.setParameter("description", irIn.getDescription()).getSingleResult();

        Assertions.assertEquals(irIn.getDescription(), ir.getDescription());

    }
}
