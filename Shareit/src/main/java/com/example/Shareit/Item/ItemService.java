package com.example.Shareit.Item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {

    ItemDTO addItem(int id, ItemDTO itemDto);

    ItemDTO updateItem(int userId, int itemId, ItemDTO itemDto);

    ItemDTO getItemById(int userId, int ItemId);

    List<ItemDTO> getAllItem(int id);

    List<ItemDTO> searchItems(String text);

    CommentDTO addComment(Comment comment, int userId, int itemId);


}
