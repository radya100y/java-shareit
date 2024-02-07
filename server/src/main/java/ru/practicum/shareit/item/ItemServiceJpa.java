package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.error.AccessException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.ValidateException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class ItemServiceJpa implements ItemService {

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final BookingService bookingService;

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemDto save(ItemDto item, long userId) {
        userService.get(userId);
        ItemRequest ir;
        if (item.getRequestId() == null) ir = null;
        else ir = itemRequestRepository.findById(item.getRequestId()).orElse(null);
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(item, ir)));
    }

    @Override
    public ItemDto get(long id, long userId) {
        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Вещь с идентификатором " + id + " не найдена")));
        if (itemDto.getOwner() == userId) {
            Booking lastBooking = bookingService.getBookingByItemOld(id).stream().findFirst().orElse(null);
            Booking nextBooking = bookingService.getBookingByItemNew(id, BookingStatus.APPROVED).stream()
                    .findFirst().orElse(null);
            if (lastBooking != null) itemDto.setLastBooking(ItemMapper.toBookingSmall(lastBooking));
            if (nextBooking != null) itemDto.setNextBooking(ItemMapper.toBookingSmall(nextBooking));
        }
        itemDto.setComments(commentRepository.findAllByItem_IdOrderById(id).stream()
                .map(CommentMapper::toCommentResponseFromComment)
                .collect(Collectors.toList())
        );
        return itemDto;
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto item, long userId) {
        userService.get(userId);
        if (item.getOwner() != userId)
            throw new AccessException("Пользователь " + userId + " не владелец вещи " + item.getId());

        ItemDto savedItem = get(item.getId(), userId);
        if (item.getAvailable() == null) item.setAvailable(savedItem.getAvailable());
        if (item.getName() == null) item.setName(savedItem.getName());
        if (item.getDescription() == null) item.setDescription(savedItem.getDescription());

        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(item, null)));//Вот это косяк запросом
    }

    @Override
    @Transactional
    public void delete(long id, long userId) {
        userService.get(userId);
        if (get(id, userId).getOwner() != userId)
            throw new AccessException("Пользователь " + userId + " не владелец вещи " + id);
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> getAll(long userId) {
        return itemRepository.findAllByOwner(userId).stream()
                .map(ItemMapper::toItemDto)
                .map(x -> {
                    Booking lastBooking = bookingService.getBookingByItemOld(x.getId()).stream()
                            .findFirst().orElse(null);
                    Booking nextBooking = bookingService.getBookingByItemNew(x.getId(), BookingStatus.APPROVED).stream()
                            .findFirst().orElse(null);
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
    @Transactional
    public CommentResponse addComment(CommentRequest commentRequest) {

        Item item = itemRepository.findById(commentRequest.getItemId()).orElseThrow(() ->
                new NotFoundException("Вещь с идентификатором " + commentRequest.getItemId() + " не найдена"));

        User user = userService.getModel(commentRequest.getUserId());

        List<Booking> bookings = bookingService.getBookingByItemUserStatusOld(commentRequest.getItemId(),
                commentRequest.getUserId());
        if (bookings.isEmpty())
            throw new ValidateException("У пользователя с идентификатором " +
                commentRequest.getUserId() + " не найдено ни одного завершенного бронирования");

        return CommentMapper.toCommentResponseFromComment(
                commentRepository.save(CommentMapper.toCommentFromRequest(commentRequest, item, user)));
    }

    @Override
    public List<ItemDto> getItemsForRequest(Long requestId) {
        return itemRepository.findAllByRequest_IdOrderByIdDesc(requestId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
