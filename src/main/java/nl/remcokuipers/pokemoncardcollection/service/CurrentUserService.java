package nl.remcokuipers.pokemoncardcollection.service;


import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new AuthenticationCredentialsNotFoundException(
                    "No authenticated JWT found");
        }

        String keycloakId = jwt.getSubject();
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> createLocalUser(jwt));
        return user;
    }

    private User createLocalUser(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        String keycloakId = jwt.getSubject();
        User user = new User(username, email);
        user.setKeycloakId(keycloakId);
        return userRepository.save(user);
    }
}
