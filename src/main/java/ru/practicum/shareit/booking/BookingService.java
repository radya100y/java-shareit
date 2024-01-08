package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.ValidateException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final UserRepository userRepository;

    private void validate(BookingDto bookingDto) {

        if (userRepository.findById(bookingDto.getUserId()).isEmpty())
            throw new NotFoundException("Пользователь " + bookingDto.getUserId() + " не найден");

        if (bookingDto.getStart() == null || bookingDto.getEnd() == null)
            throw new ValidateException("Некорректно указаны даты начала или окончания бронирования");

        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().isEqual(bookingDto.getEnd()))
            throw new ValidateException("Дата начала бронирования должна быть меньше даты окончания бронирования");

        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());

        if ((item.isEmpty()) || (!item.get().getAvailable()))
            throw new NotFoundException("Вещь " + bookingDto.getItemId() + " не найдена или недоступна");
    }

    public BookingDto save(BookingDto bookingDto) {
        bookingDto.setStatus(BookingStatus.WAITING);
        validate(bookingDto);
        return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking(bookingDto)));
    }
}
