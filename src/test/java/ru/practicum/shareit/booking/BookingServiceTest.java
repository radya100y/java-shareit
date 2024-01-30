package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookingServiceTest {

    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    private BookingService bookingService;

    private final UserDto userDto = UserDto.builder().id(1L).email("qwe@qwe.qwe").name("qwe").build();
    private final User user = User.builder().id(1L).email("qwe@qwe.qwe").name("qwe").build();
    private final ItemDto itemDto = ItemDto.builder().id(1L).name("wer").description("wer").available(false).build();
    private final Item item = Item.builder().id(1L).name("qwe").description("qwe").available(true).owner(1L).build();
    private final BookingDto bookingDto = BookingDto.builder().id(1L).itemId(1L).start(LocalDateTime.now())
            .end(LocalDateTime.now()).build();
    private final Booking booking = Booking.builder().id(1).item(item).start(LocalDateTime.now())
            .end(LocalDateTime.now()).status(BookingStatus.WAITING).booker(user).build();

    @BeforeEach
    private void setup() {
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);

        bookingService = new BookingService(bookingRepository, itemRepository, userRepository);
    }

    @Test
    @DisplayName("Запись бронирования")
    void shouldSave() {
            when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
            when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
            when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

            bookingDto.setStart(LocalDateTime.parse("2024-02-02T00:00:00"));
            bookingDto.setEnd(LocalDateTime.parse("2024-03-03T00:00:00"));

            Assertions.assertEquals(bookingService.save(bookingDto).getBooker().getId(), 1);
    }

    @Test
    @DisplayName("Обновление бронирования")
    void shouldUpdate() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        bookingDto.setStart(LocalDateTime.parse("2024-02-02T00:00:00"));
        bookingDto.setEnd(LocalDateTime.parse("2024-03-03T00:00:00"));
        bookingDto.setUserId(2L);

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.save(bookingDto));
    }

    @Test
    @DisplayName("ПРосмотр бронирования")
    void shouldGet() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        bookingDto.setStart(LocalDateTime.parse("2024-02-02T00:00:00"));
        bookingDto.setEnd(LocalDateTime.parse("2024-03-03T00:00:00"));
        Assertions.assertEquals(bookingService.getCertain(1L, 1L).getStatus(), BookingStatus.WAITING);
    }
}
