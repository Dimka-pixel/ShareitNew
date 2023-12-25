package com.example.Shareit.User;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto getUserById(int id);

    UserDto updateUser(UserDto userDTO, int id);

    void deleteUser(int id);

    List<UserDto> getAllUsers();
}
