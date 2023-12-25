package com.example.Shareit.Booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    Booking findById(int id);

    List<Booking> findByItemIdAndBookerIdLikeAndEndIsBefore(int ItemId, int bookerId, LocalDateTime end);

    List<Booking> findByBookerIdOrderByStartDesc(int bookerId);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(int bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(int bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartAsc(int bookerId, LocalDateTime start,
                                                                             LocalDateTime end);

    List<Booking> findByBookerIdAndStatusIsLikeOrderByStartDesc(int bookerId, Status status);

    List<Booking> findBookingByItemOwnerIdIsLikeAndEndIsBeforeOrderByStartDesc(int bookerId, LocalDateTime end);

    List<Booking> findBookingByItemOwnerIdIsLikeOrderByStartDesc(int bookerId);

    List<Booking> findBookingByItemOwnerIdIsLikeAndStartIsAfterOrderByStartDesc(int bookerId, LocalDateTime start);

    List<Booking> findBookingByItemOwnerIdIsLikeAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int bookerId,
                                                                                              LocalDateTime start,
                                                                                              LocalDateTime end);

    List<Booking> findBookingByItemOwnerIdIsLikeAndStatusIsLikeOrderByStartDesc(int bookerId, Status status);

    Booking findFirstByItemIdAndEndIsBeforeOrderByStartDesc(int ItemId, LocalDateTime end);

    Booking findFirstByItemIdAndStartIsAfterOrderByStartAsc(int ItemId, LocalDateTime end);

}
