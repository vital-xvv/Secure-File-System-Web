package ua.vital.securefilesystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.vital.securefilesystem.dto.user_dto.CreateUserDTO;
import ua.vital.securefilesystem.model.User;
import ua.vital.securefilesystem.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.retrieveFullListOfUsers();
    }

    @PostMapping
    public User createNewUser(@Valid @RequestBody CreateUserDTO userDTO){
        return userService.createUser(userDTO);
    }

    @PutMapping("/{id}")
    public User updateNewUser(@PathVariable Integer id,
                              @Valid @RequestBody CreateUserDTO userDTO){
        return userService.updateUser(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
    }
}