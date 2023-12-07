package com.example.Shareit.Booking;

import com.example.Shareit.Item.ItemView;
import com.example.Shareit.User.UserView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingView {

    int id;

    LocalDateTime start;

    LocalDateTime end;

    UserView booker;

    ItemView item;

    Status status;
}
