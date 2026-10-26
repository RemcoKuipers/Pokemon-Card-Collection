package nl.remcokuipers.pokemoncardcollection.service;


import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.exception.DuplicateResourceException;
import nl.remcokuipers.pokemoncardcollection.exception.IncompleteUserAccountException;
import nl.remcokuipers.pokemoncardcollection.exception.UserBlockedException;
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
        if (!user.isEnabled()){
            throw new UserBlockedException("User account is blocked");
        }
        return user;
    }

    private User createLocalUser(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null || username.isBlank()) {
            throw new IncompleteUserAccountException("No username found");
        }
        String email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()) {
            throw new IncompleteUserAccountException(
                    "No email found");
        }
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username is already in use");
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email is already in use");
        }
        String keycloakId = jwt.getSubject();
        User user = new User(username, email);
        user.setKeycloakId(keycloakId);
        return userRepository.save(user);
    }
}
