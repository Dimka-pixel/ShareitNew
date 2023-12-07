package com.example.Shareit.Booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.Shareit.Item.ItemController.HEADER_NAME;

@RestController
@RequestMapping("/bookings")
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    BookingView addBooking(@Validated @RequestBody BookingDTO bookingDTO, @RequestHeader(value = HEADER_NAME) int userId) {
        log.info(" request POST/bookings ");
        return bookingService.addBooking(bookingDTO, userId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(value = HttpStatus.OK)
    BookingView updateBookingStatus(@RequestHeader(value = HEADER_NAME) int userId, @PathVariable int bookingId,
                                    @RequestParam boolean approved) {
        log.info(" request PATCH/bookings/{bookingId} ");
        return bookingService.updateBookingStatus(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(value = HttpStatus.OK)
    BookingView getBookingById(@RequestHeader(value = HEADER_NAME) int userId, @PathVariable int bookingId) {
        log.info(" request GET/bookings/{bookingId} ");
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    List<BookingView> getBookingByUser(@RequestHeader(value = HEADER_NAME) int userId, @RequestParam(required = false) String state) {
        log.info("request GET/bookings");
        return bookingService.getAllBookingByUser(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(value = HttpStatus.OK)
    List<BookingView> getBookingByItem(@RequestHeader(value = HEADER_NAME) int userId, @RequestParam(required = false) String state) {
        log.info("request GET/bookings/owner");
        return bookingService.getAllBookingByItem(userId, state);
    }

}
