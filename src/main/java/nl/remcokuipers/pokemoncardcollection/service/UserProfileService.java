package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.dto.UserProfileRequestDTO;
import nl.remcokuipers.pokemoncardcollection.dto.UserProfileResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.UserProfileUpdateDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.entity.UserProfile;
import nl.remcokuipers.pokemoncardcollection.exception.DuplicateResourceException;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.mapper.UserProfileMapper;
import nl.remcokuipers.pokemoncardcollection.repository.UserProfileRepository;
import org.springframework.stereotype.Service;


@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileService(UserProfileRepository userProfileRepository, UserProfileMapper userProfileMapper) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileMapper = userProfileMapper;
    }

    public UserProfileResponseDTO addUserProfile(UserProfileRequestDTO dto, User user) {
        if (userProfileRepository.existsByUser(user)) {
            throw new DuplicateResourceException("User already has a profile.");
        }
        UserProfile userProfile =
                userProfileMapper.mapToEntity(dto, user);

        UserProfile savedUserProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.mapToResponseDTO(savedUserProfile);
    }

    public UserProfileResponseDTO getUserProfileByUser(User user) {

        UserProfile userProfile = userProfileRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User profile does not exist."
                        )
                );

        return userProfileMapper.mapToResponseDTO(userProfile);
    }

    public UserProfileResponseDTO updateUserProfile(
            UserProfileUpdateDTO dto,
            User user
    ) {
        UserProfile userProfile =
                userProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User profile does not exist."
                                )
                        );
        userProfile.setCountry(dto.country());
        userProfile.setGender(dto.gender());
        userProfile.setDisplayName(dto.displayName());

        UserProfile savedUserProfile = userProfileRepository.save(userProfile);
        return userProfileMapper.mapToResponseDTO(savedUserProfile);
    }

    public void deleteUserProfile(Long id, User user) {
        UserProfile userProfile =
                userProfileRepository.findByIdAndUser(id, user)
                        .orElseThrow(() -> new ResourceNotFoundException("User profile with id " + id + " does not exist"));
        userProfileRepository.delete(userProfile);
    }
}


