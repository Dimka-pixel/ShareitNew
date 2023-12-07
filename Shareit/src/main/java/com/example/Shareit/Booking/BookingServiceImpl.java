package com.example.Shareit.Booking;

import com.example.Shareit.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public BookingView addBooking(BookingDTO bookingDTO, int userId) {
        bookingDTO.setBookerId(userId);
        Booking booking = bookingMapper.toBooking(bookingDTO);
        if (booking.getItem() != null) {
            if (booking.getItem().isAvailable()) {
                if (booking.getBooker() != null) {
                    if (booking.getStart() != booking.getEnd() && booking.getEnd().isAfter(booking.getStart())) {
                        if (userId != booking.getItem().getOwner().getId()) {
                            booking.setStatus(Status.WAITING);
                            booking = bookingRepository.save(booking);
                            log.info("return: booking + {}", booking);
                            return bookingMapper.toBookingView(booking);
                        } else {
                            log.warn("booking request from the owner");
                            throw new BookingValidateException("You can't book your items", HttpStatus.NOT_FOUND);
                        }
                    } else {
                        log.warn("Dates is uncorrected: {} {} ", booking.getStart(), booking.getEnd());
                        throw new BookingValidateException("Dates is uncorrected", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    log.warn("User with id {} not found", userId);
                    throw new BookingValidateException("User not found", HttpStatus.NOT_FOUND);
                }
            } else {
                log.warn("item is_available = false ");
                throw new BookingValidateException("The owner does not allow booking this item", HttpStatus.BAD_REQUEST);
            }
        } else {
            log.warn("item with id {} not found", bookingDTO.getItemId());
            throw new BookingValidateException("The item not found", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @Override
    public BookingView updateBookingStatus(int bookingId, int userId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId);
        if (userId == booking.getItem().getOwner().getId()) {
            if (booking.getStatus().equals(Status.WAITING)) {
                if (approved) {
                    booking.setStatus(Status.APPROVED);
                } else {
                    booking.setStatus(Status.REJECTED);
                }
            } else {
                log.warn("You can't change status after update Status");
                throw new BookingValidateException("You can't change status", HttpStatus.BAD_REQUEST);
            }
        } else {
            log.warn("User is not the owner this Item");
            throw new BookingValidateException("You are not the owner this Item", HttpStatus.NOT_FOUND);
        }
        log.info("return: {}", booking);
        return bookingMapper.toBookingView(booking);
    }

    @Override
    public BookingView getBookingById(int userId, int bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            log.warn("booking is null");
            throw new BookingValidateException("Booking with id = " + bookingId + " not found",
                    HttpStatus.NOT_FOUND);
        }
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            log.info("return bookingView {}", booking);
            return bookingMapper.toBookingView(booking);
        } else {
            log.warn("the user must be the owner or tenant to get this information");
            throw new BookingValidateException("You do not have sufficient rights to receive this information",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<BookingView> getAllBookingByUser(int userId, String requestState) {
        LocalDateTime dateByOrder = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();
        List<BookingView> bookingViews = new ArrayList<>();
        if (userRepository.existsById(userId)) {
            if (requestState != null) {
                bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
                State state = State.valueOf(requestState);
                switch (state) {
                    case WAITING:
                        bookings = bookingRepository.findByBookerIdAndStatusIsLikeOrderByStartDesc(userId, Status.WAITING);
                        break;
                    case FUTURE:
                        bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, dateByOrder);
                        break;
                    case PAST:
                        bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, dateByOrder);
                        break;
                    case CURRENT:
                        bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartAsc(userId,
                                dateByOrder, dateByOrder);
                        break;
                    case ALL:
                        bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
                        break;
                    case REJECTED:
                        bookings = bookingRepository.findByBookerIdAndStatusIsLikeOrderByStartDesc(userId, Status.REJECTED);
                        break;
                    default:
                        throw new BookingValidateException("Unknown state: UNSUPPORTED_STATUS", HttpStatus.NOT_FOUND);
                }

            } else {
                bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
            }
            for (Booking book : bookings) {
                bookingViews.add(bookingMapper.toBookingView(book));
            }
            log.info("return bookingViews {}", bookingViews);
            return bookingViews;
        } else {
            throw new BookingValidateException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<BookingView> getAllBookingByItem(int userId, String requestState) {
        if (userRepository.existsById(userId)) {
            LocalDateTime dateByOrder = LocalDateTime.now();
            List<Booking> bookings = new ArrayList<>();
            List<BookingView> bookingViews = new ArrayList<>();
            if (requestState != null) {
                State state = State.valueOf(requestState);
                switch (state) {
                    case WAITING:
                        bookings = bookingRepository.findBookingByItemOwnerIdIsLikeAndStatusIsLikeOrderByStartDesc(userId, Status.WAITING);
                        break;
                    case FUTURE:
                        bookings = bookingRepository.findBookingByItemOwnerIdIsLikeAndStartIsAfterOrderByStartDesc(userId, dateByOrder);
                        break;
                    case REJECTED:
                        bookings = bookingRepository.findBookingByItemOwnerIdIsLikeAndStatusIsLikeOrderByStartDesc(userId, Status.REJECTED);
                        break;
                    case PAST:
                        bookings = bookingRepository.findBookingByItemOwnerIdIsLikeAndEndIsBeforeOrderByStartDesc(userId, dateByOrder);
                        break;
                    case CURRENT:
                        bookings = bookingRepository.findBookingByItemOwnerIdIsLikeAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, dateByOrder, dateByOrder);
                        break;
                    case ALL:
                        bookings = bookingRepository.findBookingByItemOwnerIdIsLikeOrderByStartDesc(userId);
                        break;
                }
            } else {
                bookings = bookingRepository.findBookingByItemOwnerIdIsLikeOrderByStartDesc(userId);
            }
            for (Booking book : bookings) {
                bookingViews.add(bookingMapper.toBookingView(book));
            }
            log.info("return bookingViews {}", bookingViews);
            return bookingViews;
        } else {
            log.warn("User with id {} not found", userId);
            throw new BookingValidateException("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
