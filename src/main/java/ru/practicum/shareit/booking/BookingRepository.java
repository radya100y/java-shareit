package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_IdOrderByIdDesc(long bookerId);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByIdAsc(
            long bookerId, LocalDateTime pb, LocalDateTime pe);

    List<Booking> findAllByBooker_IdAndStatusAndEndIsBeforeOrderByIdDesc(
            long bookerId, BookingStatus status, LocalDateTime pb);

    List<Booking> findAllByBooker_IdAndStatusInAndStartIsAfterOrderByIdDesc(
            long bookerId, List<BookingStatus> status, LocalDateTime pb);

    List<Booking> findAllByBooker_IdAndStatusOrderByIdDesc(long bookerId, BookingStatus status);



    List<Booking> findAllByItemOwnerOrderByIdDesc(long ownerId);

    List<Booking> findAllByItemOwnerAndStartIsBeforeAndEndIsAfterOrderByIdDesc(
            long ownerId, LocalDateTime pb, LocalDateTime pe);

    List<Booking> findAllByItemOwnerAndStatusAndEndIsBeforeOrderByIdDesc(
            long ownerId, BookingStatus status, LocalDateTime pb);

    List<Booking> findAllByItemOwnerAndStatusInAndStartIsAfterOrderByIdDesc(
            long ownerId, List<BookingStatus> status, LocalDateTime pb);

    List<Booking> findAllByItemOwnerAndStatusOrderByIdDesc(long bookerId, BookingStatus status);

    List<Booking> findByItem_IdAndStartIsBeforeOrderByEndDesc(long itemId, LocalDateTime now);

    List<Booking> findByItem_IdAndStartIsAfterAndStatusOrderByStartAsc(
            long itemId, LocalDateTime now, BookingStatus status);

    List<Booking> findAllByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
            long itemId, long userId, BookingStatus status, LocalDateTime pb);
}
