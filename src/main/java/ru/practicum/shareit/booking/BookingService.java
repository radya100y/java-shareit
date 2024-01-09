package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.ValidateException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

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

    private void validate(BookingDto booking) {

        item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (booking.getStart() == null || booking.getEnd() == null)
            throw new ValidateException("Некорректно указаны даты начала или окончания бронирования");

        if (booking.getStart().isAfter(booking.getEnd()) || booking.getStart().isEqual(booking.getEnd()))
            throw new ValidateException("Дата начала бронирования должна быть меньше даты окончания бронирования");

        if (!item.getAvailable())
            throw new ValidateException("Вещь " + booking.getItemId() + " недоступна");
    }

    public Booking save(BookingDto booking) {

        validate(booking);
        return bookingRepository.save(BookingMapper.toBooking(booking, item, user));
    }

}
