package com.example.Shareit.Item;
//Pull requests
public class ItemMapper {

    public static ItemDTO mapItemToDto(Item item) {
        ItemDTO itemDto = null;

        if (item != null) {
            itemDto = ItemDTO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.is_available())
                    .build();
        }
        return itemDto;
    }

    public static Item mapDtoToItem(ItemDTO itemDto) {
        Item item = new Item();
        if (itemDto != null) {
            item = Item.builder()
                    .name(itemDto.getName())
                    .description(itemDto.getDescription())
                    .is_available(itemDto.getAvailable())
                    .build();
        }
        return item;
    }
}
