package ua.vital.securefilesystem.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.vital.securefilesystem.dto.user_dto.CreateUserDTO;
import ua.vital.securefilesystem.model.User;
import ua.vital.securefilesystem.repository.UserRepository;
import ua.vital.securefilesystem.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> retrieveFullListOfUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(CreateUserDTO dto) {
        return userRepository.save(dto.toUser());
    }

    @Override
    public User updateUser(Integer id, CreateUserDTO dto) {
        User user = dto.toUser();
        user.setId(id);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
