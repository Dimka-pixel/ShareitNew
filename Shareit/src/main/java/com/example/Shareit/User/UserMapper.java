package com.example.Shareit.User;

import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserView toUserView(User user);

    UserDTO toUserDTO(User user);

    User toUser(UserDTO userDTO);
}
