package com.example.Shareit.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable int id) {
        log.info("response GET /users/{id} id = {}", id);
        return userService.getUserById(id);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        log.info("response GET /users");
        return userService.getAllUsers();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Validated({AllMappingValidated.class, ExceptPatchMappingValidated.class})
                           @RequestBody UserDto userDto) {
        log.info("response POST /users");
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable int id) {
        log.info("response Delete /users/{id} id = {}", id);
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@Validated(AllMappingValidated.class) @RequestBody UserDto userDTO,
                              @PathVariable int id) {
        log.info("response Patch /users/{id} id = {}", id);
        return userService.updateUser(userDTO, id);
    }
}
