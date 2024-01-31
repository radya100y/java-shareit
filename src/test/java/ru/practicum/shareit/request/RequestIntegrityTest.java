package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
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

        UserDto user = UserDto.builder().email("ert@ert.ert").name("ert").build();
        UserDto saveUser = userService.save(user);

        ItemRequestDtoIn irIn = new ItemRequestDtoIn(saveUser.getId(), "qwe");
        ItemRequestDtoOut savedIr = service.save(irIn);

        TypedQuery<ItemRequest> query = em.createQuery(
                "Select r from ItemRequest r where r.description = :description", ItemRequest.class);
        ItemRequest ir = query.setParameter("description", irIn.getDescription()).getSingleResult();

        Assertions.assertEquals(irIn.getDescription(), ir.getDescription());

    }
}
