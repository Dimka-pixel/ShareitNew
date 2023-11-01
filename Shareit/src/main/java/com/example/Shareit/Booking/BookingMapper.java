package com.example.Shareit.Booking;

import com.example.Shareit.Item.ItemRepository;
import com.example.Shareit.User.User;
import com.example.Shareit.User.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
@Data
@RequiredArgsConstructor
public class BookingMapper {

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;


    public BookingDTO mapBookingToDto(Booking booking) {
        BookingDTO bookingDTO = null;
        if (booking != null) {
            bookingDTO = BookingDTO.builder()
                    .id(booking.getId())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .bookerId(booking.getBooker().getId())
                    .itemId(booking.getItem().getId())
                    .ItemName(booking.getItem().getName())
                    .status(booking.getStatus())
                    .build();
        }

        return bookingDTO;
    }

    public Booking mapDtoToBooking(BookingDTO bookingDTO) throws EntityNotFoundException {
        Booking booking = null;
        User user = userRepository.findById(bookingDTO.getBookerId());
        if (bookingDTO != null) {
            booking = Booking.builder()
                    .id(bookingDTO.getId())
                    .start(bookingDTO.getStart())
                    .end(bookingDTO.getEnd())
                    .booker(user)
                    .item(itemRepository.getReferenceById(bookingDTO.getItemId()))
                    .status(bookingDTO.getStatus())
                    .build();
        }
        return booking;
    }
}
