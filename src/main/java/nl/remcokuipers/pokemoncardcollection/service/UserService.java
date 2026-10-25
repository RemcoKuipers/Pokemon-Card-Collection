package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.dto.UserResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.UserMapper;
import nl.remcokuipers.pokemoncardcollection.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,  UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users
                .stream()
                .map(userMapper::mapToResponseDTO)
                .toList();
    }

    public UserResponseDTO getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return userMapper.mapToResponseDTO(user.get());
        } else throw new ResourceNotFoundException("User with id: " + id + " not found");
    }

}
