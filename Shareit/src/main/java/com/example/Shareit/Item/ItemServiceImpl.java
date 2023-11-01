package com.example.Shareit.Item;

import com.example.Shareit.Booking.Booking;
import com.example.Shareit.Booking.BookingMapper;
import com.example.Shareit.Booking.BookingRepository;
import com.example.Shareit.Booking.BookingValidateException;
import com.example.Shareit.User.User;
import com.example.Shareit.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDTO addItem(int id, ItemDTO itemDto) {
        Item item = ItemMapper.mapDtoToItem(itemDto);
        User user = userRepository.findById(id);
        if (user != null) {
            item.setOwner(user);
            itemRepository.save(item);
            ItemDTO itemDTO = ItemMapper.mapItemToDto(item);
            log.info("return {}", item);
            return itemDTO;
        } else {
            log.warn("User with id {} not found", id);
            throw new ItemValidateException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ItemDTO updateItem(int userId, int itemId, ItemDTO itemDto) {
        User user = userRepository.findById(userId);
        Item item = itemRepository.getReferenceById(itemId);
        if (user.equals(item.getOwner())) {
            if (itemDto.getName() != null) {
                if (!itemDto.getName().isBlank()) {
                    item.setName(itemDto.getName());
                } else {
                    log.warn("The field \"name\" is blank");
                    throw new ItemValidateException("The field \"name\" should not be empty", HttpStatus.BAD_REQUEST);
                }
            }
            if (itemDto.getDescription() != null) {
                if (!itemDto.getDescription().isBlank()) {
                    item.setDescription(itemDto.getDescription());
                } else {
                    log.warn("The field \"description\" is blank");
                    throw new ItemValidateException("The field \"description\" should not be empty", HttpStatus.BAD_REQUEST);
                }
            }
            if (itemDto.getAvailable() != null) {
                item.set_available(itemDto.getAvailable());
            }
            ItemDTO itemDTO = ItemMapper.mapItemToDto(itemRepository.save(item));
            log.info("return {}", itemDTO);
            return ItemMapper.mapItemToDto(itemRepository.save(item));
        } else {
            log.warn("This user is not owner");
            throw new ItemValidateException("This user is not owner", HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ItemDTO getItemById(int userId, int itemId) {
        LocalDateTime dateNow = LocalDateTime.now();
        List<CommentDTO> comments = new ArrayList<>();
        Item item = itemRepository.getReferenceById(itemId);
        ItemDTO itemDTO = ItemMapper.mapItemToDto(item);
        if (item.getOwner().getId() != userId) {
            log.info("return {}", itemDTO);
        } else {
            itemDTO.setLastBooking(bookingMapper.mapBookingToDto(bookingRepository.findFirstByItemIdAndEndIsBeforeOrderByStartDesc(item.getId(), dateNow)));
            itemDTO.setNextBooking(bookingMapper.mapBookingToDto(bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStartAsc(item.getId(), dateNow)));
        }
        for (Comment comment :
                commentRepository.findByItem_Id(itemId)) {
            comments.add(commentMapper.mapToCommentDTO(comment));
        }
        itemDTO.setComments(comments);
        return itemDTO;
    }

    @Override
    public List<ItemDTO> getAllItem(int id) {
        LocalDateTime dateNow = LocalDateTime.now();
        List<ItemDTO> items = new ArrayList<>();
        List<CommentDTO> comments = new ArrayList<>();
        List<Item> allItems = itemRepository.findAll();
        User user = userRepository.findById(id);
        if (user != null) {
            for (Item item : allItems) {
                if (!allItems.isEmpty() && user.equals(item.getOwner())) {
                    ItemDTO itemDTO = ItemMapper.mapItemToDto(item);
                    itemDTO.setLastBooking(bookingMapper.mapBookingToDto(bookingRepository.findFirstByItemIdAndEndIsBeforeOrderByStartDesc(item.getId(), dateNow)));
                    itemDTO.setNextBooking(bookingMapper.mapBookingToDto(bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStartAsc(item.getId(), dateNow)));
                    for (Comment comment :
                            commentRepository.findByItem_Id(item.getId())) {
                        comments.add(commentMapper.mapToCommentDTO(comment));
                    }
                    itemDTO.setComments(comments);
                    items.add(itemDTO);
                }
            }
        }
        log.info("return {}", items);
        return items;
    }

    @Override
    public List<ItemDTO> searchItems(String text) {
        List<ItemDTO> items = new ArrayList<>();
        List<Item> allItems = itemRepository.findAll();
        if (text != null) {
            for (Item item : allItems) {
                if (!text.isEmpty() && !allItems.isEmpty() && item.getDescription().toLowerCase().
                        contains(text.toLowerCase()) && item.is_available()) {
                    items.add(ItemMapper.mapItemToDto(item));
                }
            }
        } else {
            log.warn("The description is empty");
            throw new ItemValidateException("The description should not be empty", HttpStatus.BAD_REQUEST);
        }
        log.info("return {}", items);
        return items;
    }

    @Override
    public CommentDTO addComment(Comment comment, int userId, int itemId) {
        LocalDateTime dateNow = LocalDateTime.now();
        List<Booking> booking = bookingRepository.findByItemIdAndBookerIdLikeAndEndIsBefore(itemId, userId, dateNow);
        User user = userRepository.findById(userId);
        Item item = itemRepository.findById(itemId);
        if (!booking.isEmpty()) {
            comment.setCreated(LocalDateTime.now());
            comment.setItem(item);
            comment.setUser(user);
            return commentMapper.mapToCommentDTO(commentRepository.save(comment));
        } else {
            log.warn("leave a comment only after the booking is completed");
            throw new BookingValidateException("Cannot leave a comment only after the booking is completed", HttpStatus.BAD_REQUEST);
        }

    }

}
