package nl.remcokuipers.pokemoncardcollection.mapper;

import nl.remcokuipers.pokemoncardcollection.dto.UserResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(
             user.getId(),
             user.getUsername(),
             user.getEmail(),
             user.getEnabled()
        );
    }
}
