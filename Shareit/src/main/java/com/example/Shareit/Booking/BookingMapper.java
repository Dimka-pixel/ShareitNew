package com.example.Shareit.Booking;

import com.example.Shareit.Item.Item;
import com.example.Shareit.Item.ItemMapper;
import com.example.Shareit.Item.ItemRepository;
import com.example.Shareit.User.User;
import com.example.Shareit.User.UserMapper;
import com.example.Shareit.User.UserRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {UserMapper.class, ItemMapper.class}, componentModel = "spring", imports = {UserRepository.class, ItemRepository.class})
public abstract class BookingMapper {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    abstract BookingView toBookingView(Booking booking);

    public BookingDTO toBookingDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDTO.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBooker().getId())
                .itemId(booking.getItem().getId())
                .ItemName(booking.getItem().getName())
                .status(booking.getStatus())
                .build();
    }

    public Booking toBooking(BookingDTO bookingDTO) {
        Item item = itemRepository.findById(bookingDTO.getItemId());
        User user = userRepository.findById(bookingDTO.getBookerId());
        return Booking.builder()
                .id(bookingDTO.getId())
                .start(bookingDTO.getStart())
                .end(bookingDTO.getEnd())
                .booker(user)
                .item(item)
                .status(bookingDTO.getStatus())
                .build();
    }
}
