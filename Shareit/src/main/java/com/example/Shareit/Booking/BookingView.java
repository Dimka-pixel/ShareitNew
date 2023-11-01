package com.example.Shareit.Booking;

import com.example.Shareit.Item.ItemView;
import com.example.Shareit.User.UserView;

import java.time.LocalDateTime;

public interface BookingView {
    int getId();

    LocalDateTime getStart();

    LocalDateTime getEnd();

    UserView getBooker();

    ItemView getItem();

    Status getStatus();
}
