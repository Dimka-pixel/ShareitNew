package com.example.Shareit.Booking;
//Pull requests
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
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    @Override
    public BookingView addBooking(BookingDTO bookingDTO, int userId) {
        bookingDTO.setBookerId(userId);
        Booking booking = bookingMapper.mapDtoToBooking(bookingDTO);
        if (booking.getItem().is_available()) {
            if (booking.getBooker() != null) {
                if (booking.getStart() != booking.getEnd() && booking.getEnd().isAfter(booking.getStart())) {
                    if (userId != booking.getItem().getOwner().getId()) {
                        booking.setStatus(Status.WAITING);
                        booking = bookingRepository.save(booking);
                        log.info("return: booking + {}", booking);
                        return bookingRepository.getBookingById(booking.getId());
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
    }

    @Override
    public BookingView updateBookingStatus(int bookingId, int userId, boolean approved) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
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
        BookingView bookingView = bookingRepository.getBookingById(bookingId);
        log.info("return: bookingView");
        return bookingView;
    }

    @Override
    public BookingView getBookingById(int userId, int bookingId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getBooker().getId() == userId || booking.getItem().getOwner().getId() == userId) {
            BookingView bookingView = bookingRepository.getBookingById(bookingId);
            log.info("return bookingView {}", bookingView);
            return bookingView;
        } else {
            log.warn("the user must be the owner or tenant to get this information");
            throw new BookingValidateException("You do not have sufficient rights to receive this information",
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<BookingView> getAllBookingByUser(int userId, String requestState) {
        if (userRepository.findById(userId) != null) {
            LocalDateTime dateByOrder = LocalDateTime.now();
            List<BookingView> bookings = new ArrayList<>();
            if (requestState != null) {
                bookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId);
                State state = State.valueOf(requestState);
                switch (state) {
                    case WAITING:
                        bookings = bookingRepository.findByBooker_IdAndStatusIsLikeOrderByStartDesc(userId, Status.WAITING);
                        break;
                    case FUTURE:
                        bookings = bookingRepository.findByBooker_IdAndStartIsAfterOrderByStartDesc(userId, dateByOrder);
                        break;
                    case PAST:
                        bookings = bookingRepository.findByBooker_IdAndEndIsBeforeOrderByStartDesc(userId, dateByOrder);
                        break;
                    case CURRENT:
                        bookings = bookingRepository.findByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartAsc(userId,
                                dateByOrder, dateByOrder);
                        break;
                    case ALL:
                        bookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId);
                        break;
                    case REJECTED:
                        bookings = bookingRepository.findByBooker_IdAndStatusIsLikeOrderByStartDesc(userId, Status.REJECTED);
                        break;
                    default:
                        throw new BookingValidateException("Unknown state: UNSUPPORTED_STATUS", HttpStatus.NOT_FOUND);
                }
            } else {
                bookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId);
            }
            return bookings;
        } else {
            throw new BookingValidateException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<BookingView> getAllBookingByItem(int userId, String requestState) {
        if (userRepository.findById(userId) != null) {
            LocalDateTime dateByOrder = LocalDateTime.now();
            List<BookingView> bookings = new ArrayList<>();
            if (requestState != null) {
                State state = State.valueOf(requestState);
                switch (state) {
                    case WAITING:
                        bookings = bookingRepository.findBookingByItem_ownerIdIsLikeAndStatusIsLikeOrderByStartDesc(userId, Status.WAITING);
                        break;
                    case FUTURE:
                        bookings = bookingRepository.findBookingByItem_ownerIdIsLikeAndStartIsAfterOrderByStartDesc(userId, dateByOrder);
                        break;
                    case REJECTED:
                        bookings = bookingRepository.findBookingByItem_ownerIdIsLikeAndStatusIsLikeOrderByStartDesc(userId, Status.REJECTED);
                        break;
                    case PAST:
                        bookings = bookingRepository.findBookingByItem_ownerIdIsLikeAndEndIsBeforeOrderByStartDesc(userId, dateByOrder);
                        break;
                    case CURRENT:
                        bookings = bookingRepository.findBookingByItem_ownerIdIsLikeAndStartIsBeforeAndEndIsAfterOrderByStartDesc(userId, dateByOrder, dateByOrder);
                        break;
                    case ALL:
                        bookings = bookingRepository.findBookingByItem_ownerIdIsLikeOrderByStartDesc(userId);
                        break;
                }
            } else {
                bookings = bookingRepository.findBookingByItem_ownerIdIsLikeOrderByStartDesc(userId);
            }
            log.info("return bookings {}", bookings);
            return bookings;
        } else {
            log.warn("User with id {} not found", userId);
            throw new BookingValidateException("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
