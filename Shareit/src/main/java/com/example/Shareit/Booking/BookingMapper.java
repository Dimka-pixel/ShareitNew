package com.example.Shareit.Booking;

import com.example.Shareit.Item.ItemMapper;
import com.example.Shareit.Item.ItemRepository;
import com.example.Shareit.User.UserMapper;
import com.example.Shareit.User.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(uses = {UserMapper.class, ItemMapper.class}, componentModel = "spring")
public abstract class BookingMapper {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    abstract BookingView toBookingView(Booking booking);

    @Mapping(source = "booking.booker.id", target = "bookerId")
    @Mapping(source = "booking.item.id", target = "itemId")
    @Mapping(source = "booking.item.name", target = "ItemName")
    abstract public BookingDto toBookingDTO(Booking booking);

    @Mapping(target = "booker", expression = "java(userRepository.findById(bookingDTO.getBookerId()))")
    @Mapping(target = "item", expression = "java(itemRepository.findById(bookingDTO.getItemId()))")
    abstract public Booking toBooking(BookingDto bookingDTO);
}
