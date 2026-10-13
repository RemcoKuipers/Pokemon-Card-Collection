package nl.remcokuipers.pokemoncardcollection.controller;

import nl.remcokuipers.pokemoncardcollection.exception.GlobalExceptionHandler;
import nl.remcokuipers.pokemoncardcollection.exception.IncompleteUserAccountException;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import nl.remcokuipers.pokemoncardcollection.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    @Mock
    private CurrentUserService currentUserService;
    @Mock
    private UserProfileService userProfileService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UserProfileController controller =
                new UserProfileController(userProfileService, currentUserService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getUserProfile_metOnvolledigAccount_geeft403MetJson() throws Exception {
        // Arrange
        when(currentUserService.getCurrentUser())
                .thenThrow(new IncompleteUserAccountException("No email found"));

        // Act & Assert
        mockMvc.perform(get("/userprofiles"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("No email found"));

        verifyNoInteractions(userProfileService);
    }
}