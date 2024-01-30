package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.error.AccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ItemServiceTest {

    private ItemRepository itemRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository itemRequestRepository;
    private UserService userService;
    private BookingService bookingService;

    private ItemService itemService;

    UserDto userDto = UserDto.builder().id(1L).email("qwe@qwe.qwe").name("qwe").build();
    User user = User.builder().id(1L).email("qwe@qwe.qwe").name("qwe").build();
    Item item = Item.builder()
            .id(1L)
            .name("qwe")
            .description("qwe")
            .available(true)
            .owner(1L)
            .build();

    ItemDto itemDto = ItemDto.builder().id(1L).name("wer").description("wer").available(false).build();

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemRequestRepository = mock(ItemRequestRepository.class);
        userService = mock(UserService.class);
        bookingService = mock(BookingService.class);

        itemService = new ItemServiceJpa(itemRepository, userService, bookingService, commentRepository,
                itemRequestRepository);
    }

    @Test
    @DisplayName("Возвращаем вещь")
    void shouldGet() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingService.getBookingByItemOld(anyLong()))
                .thenReturn(List.of(Booking.builder()
                        .id(1L).item(item).start(LocalDateTime.parse("2024-01-01T00:00:00"))
                        .end(LocalDateTime.parse("2024-02-02T00:00:00")).status(BookingStatus.WAITING).booker(user)
                        .build()));

        when(bookingService.getBookingByItemNew(anyLong(), any(BookingStatus.class)))
                .thenReturn(List.of(Booking.builder().id(2L).item(item)
                        .start(LocalDateTime.parse("2024-02-02T00:00:00"))
                        .end(LocalDateTime.parse("2024-03-03T00:00:00")).status(BookingStatus.WAITING).booker(user)
                        .build()));

        Assertions.assertEquals(itemService.get(1L, 1L).getOwner(), 1L);

        Assertions.assertEquals(itemService.get(1L, 1L).getLastBooking().getId(), 1L);
    }

    @Test
    @DisplayName("Обновление вещи")
    void shouldUpdate() {
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        Item updItem = item;
        updItem.setDescription("wer");
        when(itemRepository.save(any(Item.class))).thenReturn(updItem);

        Assertions.assertThrows(AccessException.class, () -> itemService.update(itemDto, 2L));
    }

    @Test
    @DisplayName("Удаление вещи")
    void shouldDelete(){
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(AccessException.class, () -> itemService.delete(1L, 2L));
    }
}
