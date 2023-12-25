package com.example.Shareit.Item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {

    ItemDto addItem(int id, ItemDto itemDto);

    ItemDto updateItem(int userId, int itemId, ItemDto itemDto);

    ItemDto getItemById(int userId, int ItemId);

    List<ItemDto> getAllItemsByOwnerId(int id);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Comment comment, int userId, int itemId);


}
