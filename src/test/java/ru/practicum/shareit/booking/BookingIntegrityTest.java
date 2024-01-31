package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Transactional
@Rollback(value = false)
@SpringBootTest(properties = "db.name=test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingIntegrityTest {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final EntityManager em;

    @Test
    void shouldSave() {

        UserDto owner = UserDto.builder().email("qwe@qwe.qwe").name("qwe").build();
        UserDto savedOwner = userService.save(owner);

        UserDto booker = UserDto.builder().email("wer@wer.wer").name("wer").build();
        UserDto savedBooker = userService.save(booker);

        ItemDto item1 = ItemDto.builder().name("qwe").description("qwe").available(true).owner(savedOwner.getId())
                .build();
        ItemDto savedItem = itemService.save(item1, savedOwner.getId());

        BookingDto booking = BookingDto.builder().itemId(savedItem.getId())
                .start(LocalDateTime.now().plus(10, ChronoUnit.HOURS))
                .end(LocalDateTime.now().plus(20, ChronoUnit.HOURS))
                .userId(savedBooker.getId()).status(BookingStatus.WAITING).build();
        bookingService.save(booking);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b where id = :id", Booking.class);
        List<Booking> bookings = query.setParameter("id", 1L).getResultList();

        Assertions.assertEquals(bookings.get(0).getBooker().getId(), savedBooker.getId());
    }
}
