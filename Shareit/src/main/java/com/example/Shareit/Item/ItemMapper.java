package com.example.Shareit.Item;

import org.mapstruct.Mapper;

@Mapper(uses = CommentMapper.class, componentModel = "spring")
public interface ItemMapper {
    ItemView toItemView(Item item);

    Item toItem(ItemDTO itemDTO);

    ItemDTO toItemDTO(Item item);
}
