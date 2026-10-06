package nl.remcokuipers.pokemoncardcollection.controller;

import nl.remcokuipers.pokemoncardcollection.dto.UserProfileResponseDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import nl.remcokuipers.pokemoncardcollection.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/userprofiles")
public class UserProfileController {
    private final UserProfileService userProfileService;
    private final CurrentUserService currentUserService;

    public UserProfileController(UserProfileService userProfileService, CurrentUserService currentUserService) {
        this.userProfileService = userProfileService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponseDTO> getUserProfile() {
        User user = currentUserService.getCurrentUser();
        return ResponseEntity.ok(userProfileService.getUserProfileByUser(user));
    }
}
