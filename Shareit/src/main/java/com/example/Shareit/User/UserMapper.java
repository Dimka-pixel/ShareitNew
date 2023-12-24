package com.example.Shareit.User;

import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserView toUserView(User user);

    UserDto toUserDTO(User user);

    User toUser(UserDto userDTO);
}
