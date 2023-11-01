package com.example.Shareit.User;
//Pull requests
import java.util.List;

public interface UserService {

    UserDTO addUser(UserDTO userDto);

    UserDTO getUserById(int id);

    UserDTO updateUser(UserDTO userDTO, int id);

    void deleteUser(int id);

    public List<UserDTO> getAllUsers();
}
