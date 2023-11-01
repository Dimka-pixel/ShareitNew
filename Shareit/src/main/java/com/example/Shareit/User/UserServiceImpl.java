package com.example.Shareit.User;
//Pull requests
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDTO addUser(UserDTO userDto) {
        User user = UserMapper.mapDtoToUser(userDto);
        log.info("return {}", userDto);
        return UserMapper.mapUserToDto(userRepository.save(user));
    }

    @Override
    public UserDTO getUserById(int id) {
        User user = userRepository.getReferenceById(id);
        UserDTO userDTO;
        if (user != null) {
            userDTO = UserMapper.mapUserToDto(user);
        } else {
            log.warn("User with id = {} not found", id);
            throw new UserValidateException("User with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        log.info("return {}", userDTO);
        return userDTO;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, int id) {
        User user = userRepository.getReferenceById(id);
        User userWithEmail = userRepository.findByEmail(userDTO.getEmail());
        if (userDTO.getEmail() != null) {
            if (!userDTO.getEmail().isBlank()) {
                if (userWithEmail == null || userWithEmail.getId() == user.getId()) {
                    user.setEmail(userDTO.getEmail());
                } else {
                    log.warn("The field \"Email\" is exist");
                    throw new UserValidateException("The field \"Email\" already exists", HttpStatus.CONFLICT);
                }
            } else {
                log.warn("The field \"Email\" is blank");
                throw new UserValidateException("The field \"Email\" should not be empty", HttpStatus.BAD_REQUEST);
            }
        }
        if (userDTO.getName() != null) {
            if (!userDTO.getName().isBlank()) {
                user.setName(userDTO.getName());
            } else {
                log.warn("The field \"name\" is blank");
                throw new UserValidateException("The field \"name\" should not be empty", HttpStatus.BAD_REQUEST);
            }
        }
        userRepository.save(user);
        return UserMapper.mapUserToDto(user);
    }

    @Override
    public void deleteUser(int id) {
        log.info("Delete user with id = {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        List<User> allUsers = userRepository.findAll();
        if (!allUsers.isEmpty()) {
            for (User user : allUsers) {
                users.add(UserMapper.mapUserToDto(user));
            }
        }
        log.info("return {}", users);
        return users;
    }

}
