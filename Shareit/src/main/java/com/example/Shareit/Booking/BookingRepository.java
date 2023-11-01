package com.example.Shareit.Booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    BookingView getBookingById(int id);

    List<Booking> findByItemIdAndBookerIdLikeAndEndIsBefore(int ItemId, int bookerId, LocalDateTime end);

    List<BookingView> findByBooker_IdOrderByStartDesc(int bookerId);

    List<BookingView> findByBooker_IdAndEndIsBeforeOrderByStartDesc(int bookerId, LocalDateTime end);

    List<BookingView> findByBooker_IdAndStartIsAfterOrderByStartDesc(int bookerId, LocalDateTime end);

    List<BookingView> findByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartAsc(int bookerId, LocalDateTime start, LocalDateTime end);

    List<BookingView> findByBooker_IdAndStatusIsLikeOrderByStartDesc(int bookerId, Status status);

    List<BookingView> findBookingByItem_ownerIdIsLikeAndEndIsBeforeOrderByStartDesc(int bookerId, LocalDateTime end);

    List<BookingView> findBookingByItem_ownerIdIsLikeOrderByStartDesc(int bookerId);

    List<BookingView> findBookingByItem_ownerIdIsLikeAndStartIsAfterOrderByStartDesc(int bookerId, LocalDateTime start);

    List<BookingView> findBookingByItem_ownerIdIsLikeAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int bookerId, LocalDateTime start, LocalDateTime end);

    List<BookingView> findBookingByItem_ownerIdIsLikeAndStatusIsLikeOrderByStartDesc(int bookerId, Status status);

    Booking findFirstByItemIdAndEndIsBeforeOrderByStartDesc(int ItemId, LocalDateTime end);

    Booking findFirstByItemIdAndStartIsAfterOrderByStartAsc(int ItemId, LocalDateTime end);


}
