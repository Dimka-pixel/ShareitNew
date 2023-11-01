package com.example.Shareit.User;

public class UserMapper {

    public static UserDTO mapUserToDto(User user) {
        UserDTO userDto = new UserDTO();
        if (user != null) {
            userDto = UserDTO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        }
        return userDto;
    }

    public static User mapDtoToUser(UserDTO userDto) {
        User user = new User();
        if (userDto != null) {
            user = User.builder()
                    .id(userDto.getId())
                    .name(userDto.getName())
                    .email(userDto.getEmail())
                    .build();
        }
        return user;
    }
}
