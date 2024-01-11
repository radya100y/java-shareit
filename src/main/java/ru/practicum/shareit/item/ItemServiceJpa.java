package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.error.AccessException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class ItemServiceJpa implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final CommentRepository commentRepository;


    @Override
    public ItemDto save(ItemDto item, long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));

        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(item)));
    }

    @Override
    public ItemDto get(long id, long userId) {
        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Вещь с идентификатором " + id + " не найдена")));
        if (itemDto.getOwner() == userId) {
            Booking lastBooking = bookingRepository.findByItem_IdAndEndIsBeforeOrderByEndDesc(
                    id, LocalDateTime.now()).stream().findFirst().orElse(null);
            Booking nextBooking = bookingRepository.findByItem_IdAndStartIsAfterOrderByStartAsc(
                    id, LocalDateTime.now()).stream().findFirst().orElse(null);
            if (lastBooking != null) itemDto.setLastBooking(ItemMapper.toBookingSmall(lastBooking));
            if (nextBooking != null) itemDto.setNextBooking(ItemMapper.toBookingSmall(nextBooking));
        }
        return itemDto;
    }

    @Override
    public ItemDto update(ItemDto item, long userId) {
        if (userRepository.findById(userId).isEmpty())
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден");
        if (item.getOwner() != userId)
            throw new AccessException("Пользователь " + userId + " не владелец вещи " + item.getId());

        ItemDto savedItem = get(item.getId(), userId);
        if (item.getAvailable() == null) item.setAvailable(savedItem.getAvailable());
        if (item.getName() == null) item.setName(savedItem.getName());
        if (item.getDescription() == null) item.setDescription(savedItem.getDescription());

        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(item)));
    }

    @Override
    public void delete(long id, long userId) {
        if (userRepository.findById(userId).isEmpty())
            throw new NotFoundException("Пользователь с идентификатором " + userId + " не найден");
        if (get(id, userId).getOwner() != userId)
            throw new AccessException("Пользователь " + userId + " не владелец вещи " + id);
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        return itemRepository.findAllByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .map(x -> {
                    Booking lastBooking = bookingRepository.findByItem_IdAndEndIsBeforeOrderByEndDesc(
                            x.getId(), LocalDateTime.now()).stream().findFirst().orElse(null);
                    Booking nextBooking = bookingRepository.findByItem_IdAndStartIsAfterOrderByStartAsc(
                            x.getId(), LocalDateTime.now()).stream().findFirst().orElse(null);
                    if (lastBooking != null) x.setLastBooking(ItemMapper.toBookingSmall(lastBooking));
                    if (nextBooking != null) x.setNextBooking(ItemMapper.toBookingSmall(nextBooking));
                    return x;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String query) {
        if (query.isBlank()) return List.of();
        return itemRepository.getByNameOrDescrAndAvail(query).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse addComment(CommentRequest commentRequest) {

        Item item = itemRepository.findById(commentRequest.getItemId()).orElseThrow(() ->
                new NotFoundException("Вещь с идентификатором " + commentRequest.getItemId() + " не найдена"));

        User user = userRepository.findById(commentRequest.getUserId()).orElseThrow(() ->
                new NotFoundException("Пользователь с идентификатором " + commentRequest.getUserId() + " не найден"));

        List<Booking> bookings = bookingRepository.findAllByItem_IdAndBooker_IdAndStatusAndEndIsAfter(
                commentRequest.getItemId(), commentRequest.getUserId(), BookingStatus.APPROVED, LocalDateTime.now());
        if (bookings.isEmpty())
            throw new NotFoundException("У пользователя с идентификатором " +
                commentRequest.getUserId() + " не найдено ни одного завершенного бронирования");

        return CommentMapper.toCommentResponseFromComment(
                commentRepository.save(CommentMapper.toCommentFromRequest(commentRequest, item, user)));
    }
}
