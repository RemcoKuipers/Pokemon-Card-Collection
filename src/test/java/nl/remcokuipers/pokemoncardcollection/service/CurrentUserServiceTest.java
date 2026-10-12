package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.exception.IncompleteUserAccountException;
import nl.remcokuipers.pokemoncardcollection.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CurrentUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CurrentUserService currentUserService;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUser_zonderEmail_gooitIncompleteUserAccountException() {
        // Arrange
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject("keycloak-test-id")
                .claim("preferred_username", "testgebruiker")
                .build();
        SecurityContextHolder.getContext()
                .setAuthentication(new JwtAuthenticationToken(jwt));
        when(userRepository.findByKeycloakId("keycloak-test-id"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                IncompleteUserAccountException.class,
                ()-> currentUserService.getCurrentUser()
        );

        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    void getCurrentUser_metLegeEmail_gooitIncompleteUserAccountException() {
        // Arrange
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject("keycloak-test-id")
                .claim("preferred_username", "testgebruiker")
                .claim("email", "  ")
                .build();
        SecurityContextHolder.getContext()
                .setAuthentication(new JwtAuthenticationToken(jwt));
        when(userRepository.findByKeycloakId("keycloak-test-id"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                IncompleteUserAccountException.class,
                ()-> currentUserService.getCurrentUser()
        );

        verify(userRepository, never()).save(any(User.class));

    }
}