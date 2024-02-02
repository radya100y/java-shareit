package ru.practicum.shareit.request;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.practicum.shareit.config.AppConfig;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;


@DataJpaTest
@Import(AppConfig.class)
public class RequestRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRequestRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    @DisplayName("Проверка репозитария запросов")
    void shouldSave() {
        User user = User.builder().name("qwe").email("a1@a1.a1").build();
        User savedUser = userRepository.save(user);
        ItemRequest savedIr = ItemRequest.builder().author(savedUser).description("descr").created(LocalDateTime.now())
                .build();


        Assertions.assertNull(savedIr.getId());
        repository.save(savedIr);
        Assertions.assertNotNull(savedIr.getId());
    }
}
