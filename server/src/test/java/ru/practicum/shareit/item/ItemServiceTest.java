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
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemServiceTest {

    private ItemRepository itemRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository itemRequestRepository;
    private UserService userService;
    private BookingService bookingService;

    private ItemService itemService;

    private final UserDto userDto = UserDto.builder().id(1L).email("qwe@qwe.qwe").name("qwe").build();
    private final User user = User.builder().id(1L).email("qwe@qwe.qwe").name("qwe").build();
    private final Item item = Item.builder().id(1L).name("qwe").description("qwe").available(true).owner(1L).build();
    private final ItemDto itemDto = ItemDto.builder().id(1L).name("wer").description("wer").available(false).owner(1L)
            .build();
    private final CommentRequest commentIn = new CommentRequest(1L, 1L, "qwe");
    private final CommentResponse commentOut = new CommentResponse(1L, "qwe", "qwe",
            LocalDateTime.now());
    private final Comment comment = new Comment(1L, item, user, "qwe", LocalDateTime.now());

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
    @DisplayName("Добавить новую вещь")
    void shouldSave() {
        itemDto.setRequestId(1L);
        when(itemRequestRepository.findById(anyLong())).thenReturn(
                Optional.of(new ItemRequest(1L, user, "qwe", LocalDateTime.now())));
        when(itemRepository.save(any(Item.class))).thenReturn(ItemMapper.toItem(
                itemDto, new ItemRequest(1L, user, "qwe", LocalDateTime.now())));

        Assertions.assertEquals(itemService.save(itemDto, 1L).getRequestId(), 1L);
    }

    @Test
    @DisplayName("Получить вещь")
    void shouldGet() {
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
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

        Assertions.assertThrows(NotFoundException.class, () -> itemService.get(2L, 1L));
    }

    @Test
    @DisplayName("Обновление вещи")
    void shouldUpdate() {
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        Item updItem = item;
        assert updItem != null;
        updItem.setDescription(null);
        when(itemRepository.save(any(Item.class))).thenReturn(updItem);

        Assertions.assertNull(itemService.update(itemDto, 1L).getDescription());

        Assertions.assertThrows(AccessException.class, () -> itemService.update(itemDto, 2L));
    }

    @Test
    @DisplayName("Удаление вещи")
    void shouldDelete() {
        when(userService.get(anyLong())).thenReturn(userDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        Assertions.assertThrows(AccessException.class, () -> itemService.delete(1L, 2L));
        Assertions.assertDoesNotThrow(() -> itemService.delete(1L, 1L));
    }

    @Test
    @DisplayName("Поиск вещи")
    void shouldSearch() {
        when(itemRepository.getByNameOrDescrAndAvail("qwe")).thenReturn(List.of(item));
        when(itemRepository.getByNameOrDescrAndAvail("")).thenReturn(List.of());

        Assertions.assertEquals(itemService.search("qwe").get(0).getId(), 1L);
        Assertions.assertEquals(itemService.search("").size(), 0);
    }

    @Test
    @DisplayName("Комментарий после аренды")
    void shouldComment() {
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(bookingService.getBookingByItemUserStatusOld(anyLong(), anyLong())).thenReturn(List.of(new Booking()));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        commentIn.setItemId(2L);
        Assertions.assertThrows(NotFoundException.class, () -> itemService.addComment(commentIn));

        commentIn.setItemId(1L);
        Assertions.assertEquals(itemService.addComment(commentIn).getText(), "qwe");
    }

    @Test
    @DisplayName("Вывести все вещи")
    void shouldGetAll() {
        when(itemRepository.findAllByOwner(anyLong())).thenReturn(List.of(item));

        Assertions.assertEquals(itemService.getAll(1L).get(0).getDescription(), item.getDescription());
    }

    @Test
    @DisplayName("Фиктивный тест на маппер")
    void test_cannot_instantiate() {
        Assertions.assertThrows(InvocationTargetException.class, () -> {
            var constructor = ItemMapper.class.getDeclaredConstructor();
            Assertions.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
