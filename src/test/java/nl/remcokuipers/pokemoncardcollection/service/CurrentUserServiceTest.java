package nl.remcokuipers.pokemoncardcollection.service;

import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.exception.IncompleteUserAccountException;
import nl.remcokuipers.pokemoncardcollection.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
                () -> currentUserService.getCurrentUser()
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
                () -> currentUserService.getCurrentUser()
        );

        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    void getCurrentUser_metVolledigAccount_maaktLokaleGebruikerAan() {
        // Arrange
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject("keycloak-test-id")
                .claim("preferred_username", "testgebruiker")
                .claim("email", "testgebruiker@example.com")
                .build();
        SecurityContextHolder.getContext()
                .setAuthentication(new JwtAuthenticationToken(jwt));
        when(userRepository.findByKeycloakId("keycloak-test-id"))
                .thenReturn(Optional.empty());
        when(userRepository.save(any(User.class)))
                .thenAnswer(i -> i.getArgument(0));

        // Act
        User result = currentUserService.getCurrentUser();

        // Assert
        assertEquals("testgebruiker", result.getUsername());
        assertEquals("testgebruiker@example.com", result.getEmail());
        assertEquals("keycloak-test-id", result.getKeycloakId());

        verify(userRepository).save(any(User.class));

    }

    @Test
    void getCurrentUser_metBestaandeGebruiker_geeftBestaandeGebruikerTerug() {
        // Arrange
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject("keycloak-test-id")
                .claim("preferred_username", "testgebruiker")
                .claim("email", "testgebruiker@example.com")
                .build();
        SecurityContextHolder.getContext()
                .setAuthentication(new JwtAuthenticationToken(jwt));
        User bestaandeUser = new User(
                "testgebruiker",
                "testgebruiker@example.com"
        );
        when(userRepository.findByKeycloakId("keycloak-test-id"))
                .thenReturn(Optional.of(bestaandeUser));

        // Act
        User result = currentUserService.getCurrentUser();

        // Assert
        assertSame(bestaandeUser, result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getCurrentUser_zonderAuthenticatie_gooitAuthenticationCredentialsNotFoundException() {
        // Arrange
        SecurityContextHolder.clearContext();

        // Act & Assert
        assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> currentUserService.getCurrentUser()
        );
        verifyNoInteractions(userRepository);
    }

    @Test
    void GetCurrentUser_zonderJwtPrincipal_gooitIncompleteUserAccountException(){
        // Arrange
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testgebruiker", "test")
        );

        // Act & Assert
        assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> currentUserService.getCurrentUser()
        );
        verifyNoInteractions(userRepository);
    }
}