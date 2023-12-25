package com.example.Shareit.Item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    public static final String HEADER_NAME = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(@RequestHeader(value = HEADER_NAME) int userId, @Validated @RequestBody ItemDto itemDto) {
        log.info("request POST /items");
        return itemService.addItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@RequestHeader(value = HEADER_NAME) int userId, @PathVariable int itemId) {
        log.info("request GET /items/{ItemId} ItemId = {}", itemId);
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getAllItemsByOwnerId(@RequestHeader(value = HEADER_NAME) int userId) {
        log.info("request GET /items");
        return itemService.getAllItemsByOwnerId(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("request GET /items/search");
        return itemService.searchItems(text);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@RequestHeader(value = HEADER_NAME) int userId,
                              @PathVariable int itemId, @RequestBody ItemDto itemDto) {
        log.info("request PATCH /items/{itemId} itemId = {}", itemId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@Validated @RequestBody Comment comment, @RequestHeader(value = HEADER_NAME) int userId, @PathVariable int itemId) {
        log.info("request PATCH /items/{itemId}/comment");
        return itemService.addComment(comment, userId, itemId);
    }
}

