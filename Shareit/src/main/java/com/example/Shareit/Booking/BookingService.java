package com.example.Shareit.Booking;

import java.util.List;

public interface BookingService {

    BookingView addBooking(BookingDTO bookingDTO, int userId);

    BookingView updateBookingStatus(int bookingId, int userId, boolean approved);

    BookingView getBookingById(int bookingId, int userId);


    List<BookingView> getAllBookingByUser(int userId, String requestState);

    List<BookingView> getAllBookingByItem(int userId, String requestState);


}
