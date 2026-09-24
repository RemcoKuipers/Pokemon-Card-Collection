package nl.remcokuipers.pokemoncardcollection.mapper;

import nl.remcokuipers.pokemoncardcollection.dto.UserProfileRequestDTO;
import nl.remcokuipers.pokemoncardcollection.dto.UserProfileResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.entity.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfileResponseDTO mapToResponseDTO(UserProfile userProfile) {
        return new UserProfileResponseDTO(
                userProfile.getId(),
                userProfile.getDisplayName(),
                userProfile.getCountry(),
                userProfile.getDateOfBirth(),
                userProfile.getGender()
        );
    }

    public UserProfile mapToEntity(UserProfileRequestDTO dto, User user){
        UserProfile userProfile = new UserProfile(
                dto.displayName(),
                user
        );

        userProfile.setCountry(dto.country());
        userProfile.setDateOfBirth(dto.dateOfBirth());
        userProfile.setGender(dto.gender());

        return userProfile;
    }
}
