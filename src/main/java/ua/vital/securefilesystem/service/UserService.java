package ua.vital.securefilesystem.service;

import ua.vital.securefilesystem.dto.user_dto.CreateUserDTO;
import ua.vital.securefilesystem.model.User;

import java.util.List;

public interface UserService {
    List<User> retrieveFullListOfUsers();
    User createUser(CreateUserDTO userDTO);
    User updateUser(Integer id, CreateUserDTO userDTO);
    void deleteUser(Integer id);
}
