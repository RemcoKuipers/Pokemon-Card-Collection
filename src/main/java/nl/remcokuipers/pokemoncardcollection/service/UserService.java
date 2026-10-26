package nl.remcokuipers.pokemoncardcollection.service;

import jakarta.transaction.Transactional;
import nl.remcokuipers.pokemoncardcollection.dto.UserEnabledUpdateDTO;
import nl.remcokuipers.pokemoncardcollection.dto.UserResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.CollectionEntry;
import nl.remcokuipers.pokemoncardcollection.entity.Favorite;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.entity.UserProfile;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.UserMapper;
import nl.remcokuipers.pokemoncardcollection.repository.CollectionEntryRepository;
import nl.remcokuipers.pokemoncardcollection.repository.FavoriteRepository;
import nl.remcokuipers.pokemoncardcollection.repository.UserProfileRepository;
import nl.remcokuipers.pokemoncardcollection.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FavoriteRepository favoriteRepository;
    private final CollectionEntryRepository collectionEntryRepository;
    private final UserProfileRepository userProfileRepository;
    private final FileUploadService fileUploadService;

    public UserService(UserRepository userRepository, UserMapper userMapper, FavoriteRepository favoriteRepository, CollectionEntryRepository collectionEntryRepository, UserProfileRepository userProfileRepository, FileUploadService fileUploadService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.favoriteRepository = favoriteRepository;
        this.collectionEntryRepository = collectionEntryRepository;
        this.userProfileRepository = userProfileRepository;
        this.fileUploadService = fileUploadService;
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

    public UserResponseDTO updateUserEnabled(long id, UserEnabledUpdateDTO dto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setEnabled(dto.enabled());
            User savedUser = userRepository.save(foundUser);
            return userMapper.mapToResponseDTO(savedUser);
        } else throw new ResourceNotFoundException("User with id: " + id + " not found");

    }

    @Transactional
    public void deleteUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User foundUser = user.get();
            List<Favorite> favorites = favoriteRepository.findByUser(foundUser);
            for (Favorite favorite : favorites) {
                favoriteRepository.delete(favorite);
            }
            for (CollectionEntry collectionEntry : collectionEntryRepository.findByUser(foundUser)) {
                fileUploadService.deleteFilesByCollectionEntry(collectionEntry);
                collectionEntryRepository.delete(collectionEntry);
            }
            Optional<UserProfile> userProfile = userProfileRepository.findByUser(foundUser);
            if (userProfile.isPresent()) {
                userProfileRepository.delete(userProfile.get());
            }
            userRepository.delete(foundUser);
        } else throw new ResourceNotFoundException("User with id: " + id + " not found");
    }

}
