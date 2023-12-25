package com.example.Shareit.Item;

import com.example.Shareit.Booking.*;
import com.example.Shareit.User.User;
import com.example.Shareit.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;
    private final ItemMapper itemMapper;


    @Override
    @Transactional
    public ItemDto addItem(int id, ItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        User user = userRepository.findById(id);
        if (user != null) {
            item.setOwner(user);
            itemRepository.save(item);
            ItemDto itemDTO = itemMapper.toItemDTO(item);
            log.info("return {}", item);
            return itemDTO;
        } else {
            log.warn("User with id {} not found", id);
            throw new ItemValidateException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ItemDto updateItem(int userId, int itemId, ItemDto itemDto) {
        User user = userRepository.findById(userId);
        Item item = itemRepository.findById(itemId);
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
                item.setAvailable(itemDto.getAvailable());
            }
            ItemDto itemDTO = itemMapper.toItemDTO(item);
            log.info("return {}", itemDTO);
            return itemDTO;
        } else {
            log.warn("This user is not owner");
            throw new ItemValidateException("This user is not owner", HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ItemDto getItemById(int userId, int itemId) {
        LocalDateTime dateNow = LocalDateTime.now();
        List<CommentDto> comments = new ArrayList<>();
        Item item = itemRepository.getReferenceById(itemId);
        ItemDto itemDTO = itemMapper.toItemDTO(item);
        BookingDto nextBooking = bookingMapper.toBookingDTO(bookingRepository.
                findFirstByItemIdAndStartIsAfterOrderByStartAsc(item.getId(), dateNow));
        if (item.getOwner().getId() != userId) {
            log.info("return {}", itemDTO);
        } else {
            itemDTO.setLastBooking(bookingMapper.toBookingDTO(bookingRepository.
                    findFirstByItemIdAndEndIsBeforeOrderByStartDesc(item.getId(), dateNow)));
            if (nextBooking == null || nextBooking.getStatus() != Status.REJECTED) {
                itemDTO.setNextBooking(nextBooking);
            }
        }
        for (Comment comment :
                commentRepository.findByItemId(itemId)) {
            comments.add(commentMapper.toCommentDTO(comment));
        }
        itemDTO.setComments(comments);
        log.info("return itemDTO {} ", itemDTO);
        return itemDTO;
    }

    @Override
    public List<ItemDto> getAllItemsByOwnerId(int id) {
        LocalDateTime dateNow = LocalDateTime.now();
        List<ItemDto> items = new ArrayList<>();
        List<CommentDto> comments = new ArrayList<>();
        List<Item> allItems = itemRepository.findAll();
        User user = userRepository.findById(id);
        if (user != null) {
            for (Item item : allItems) {
                BookingDto nextBooking = bookingMapper.toBookingDTO(bookingRepository.findFirstByItemIdAndStartIsAfterOrderByStartAsc(item.getId(), dateNow));
                if (!allItems.isEmpty() && user.equals(item.getOwner())) {
                    ItemDto itemDTO = itemMapper.toItemDTO(item);
                    itemDTO.setLastBooking(bookingMapper.toBookingDTO(bookingRepository.findFirstByItemIdAndEndIsBeforeOrderByStartDesc(item.getId(), dateNow)));
                    if (nextBooking == null || nextBooking.getStatus() != Status.REJECTED) {
                        itemDTO.setNextBooking(nextBooking);
                    }
                    for (Comment comment :
                            commentRepository.findByItemId(item.getId())) {
                        comments.add(commentMapper.toCommentDTO(comment));
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
    public List<ItemDto> searchItems(String text) {
        List<ItemDto> items = new ArrayList<>();
        List<Item> allItems = itemRepository.findAll();
        if (text != null) {
            for (Item item : allItems) {
                if (!text.isEmpty() && !allItems.isEmpty() && item.getDescription().toLowerCase().
                        contains(text.toLowerCase()) && item.isAvailable()) {
                    items.add(itemMapper.toItemDTO(item));
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
    @Transactional
    public CommentDto addComment(Comment comment, int userId, int itemId) {
        LocalDateTime dateNow = LocalDateTime.now();
        List<Booking> booking = bookingRepository.findByItemIdAndBookerIdLikeAndEndIsBefore(itemId, userId, dateNow);
        User user = userRepository.findById(userId);
        Item item = itemRepository.findById(itemId);
        if (!booking.isEmpty()) {
            comment.setCreated(LocalDateTime.now());
            comment.setItem(item);
            comment.setUser(user);
            return commentMapper.toCommentDTO(commentRepository.save(comment));
        } else {
            log.warn("leave a comment only after the booking is completed");
            throw new BookingValidateException("Cannot leave a comment only after the booking is completed", HttpStatus.BAD_REQUEST);
        }

    }

}
