package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.error.AccessException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.ValidateException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final UserRepository userRepository;

    private Item item;

    private User user;

    private void validateReferences(BookingDto booking) {

        item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private void validate(BookingDto booking) {

        validateReferences(booking);

        if (booking.getStart() == null || booking.getEnd() == null)
            throw new ValidateException("Некорректно указаны даты начала или окончания бронирования");

        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd()))
            throw new ValidateException("Дата начала бронирования должна быть меньше даты окончания бронирования");

        if (!item.getAvailable())
            throw new ValidateException("Вещь " + booking.getItemId() + " недоступна");

        if (booking.getUserId() == item.getOwner())
            throw new AccessException("Вещь не может быть забронирована владельцем");
    }

    public Booking save(BookingDto booking) {

        validate(booking);
        return bookingRepository.save(BookingMapper.toBooking(booking, item, user));
    }

    public Booking update(long bookingId, long userId, BookingStatus status) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование " + bookingId + " не найдено"));
        validateReferences(BookingMapper.toBookingDto(booking));
        if (booking.getItem().getOwner() != userId) throw new AccessException("Пользователь " +
                userId + " не имеет прав на редактирование бронироввания " + bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public Booking getCertain(long bookingId, long userId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование " + bookingId + " не найдено"));
        if (!((booking.getItem().getOwner() == userId) || (booking.getBooker().getId() == userId)))
            throw new AccessException("Пользователь " + userId + " не имеет прав на просмотр бронироввания " +
                    bookingId);
        return booking;
    }

    List<Booking> getBookingForBooker(long bookerId, BookingStatusParam state) {

        userRepository.findById(bookerId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        switch (state) {
            case ALL:
                return bookingRepository.findAllByBooker_IdOrderByIdDesc(bookerId);
            case CURRENT:
                return bookingRepository.findAllByBooker_IdAndStatusAndStartIsAfterAndEndIsBeforeOrderByIdDesc(bookerId,
                        BookingStatus.APPROVED, LocalDateTime.now(), LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByBooker_IdAndStatusAndEndIsBeforeOrderByIdDesc(bookerId,
                        BookingStatus.APPROVED, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByBooker_IdAndStatusInAndStartIsAfterOrderByIdDesc(bookerId,
                        List.of(BookingStatus.APPROVED, BookingStatus.WAITING), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByBooker_IdAndStatusOrderByIdDesc(bookerId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByBooker_IdAndStatusOrderByIdDesc(bookerId, BookingStatus.REJECTED);
            default:
                throw new NotFoundException("Такого статуса нет");
        }
    }

    List<Booking> getBookingForOwner(long ownerId, BookingStatusParam state) {

        userRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemOwnerOrderByIdDesc(ownerId);
            case CURRENT:
                return bookingRepository.findAllByItemOwnerAndStatusAndStartIsAfterAndEndIsBeforeOrderByIdDesc(
                        ownerId, BookingStatus.APPROVED, LocalDateTime.now(), LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByItemOwnerAndStatusAndEndIsBeforeOrderByIdDesc(
                        ownerId, BookingStatus.APPROVED, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findAllByItemOwnerAndStatusInAndStartIsAfterOrderByIdDesc(
                        ownerId, List.of(BookingStatus.APPROVED, BookingStatus.WAITING), LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByItemOwnerAndStatusOrderByIdDesc(ownerId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerAndStatusOrderByIdDesc(ownerId, BookingStatus.REJECTED);
            default:
                throw new NotFoundException("Такого статуса нет");
        }
    }

}
