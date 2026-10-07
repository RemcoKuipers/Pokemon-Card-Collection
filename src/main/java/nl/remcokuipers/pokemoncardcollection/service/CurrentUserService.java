package nl.remcokuipers.pokemoncardcollection.service;


import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.exception.ResourceNotFoundException;
import nl.remcokuipers.pokemoncardcollection.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
// TODO: testgebruiker vervangen door ingelogde gebruiker via keycloak na les volgende week!
    public User getCurrentUser() {
        User user = userRepository.findById(1L)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Test user not found with id: 1"));
        return user;
    }
}
