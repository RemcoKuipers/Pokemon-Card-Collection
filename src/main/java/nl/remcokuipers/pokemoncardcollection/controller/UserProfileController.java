package nl.remcokuipers.pokemoncardcollection.controller;

import jakarta.validation.Valid;
import nl.remcokuipers.pokemoncardcollection.dto.UserProfileRequestDTO;
import nl.remcokuipers.pokemoncardcollection.dto.UserProfileResponseDTO;
import nl.remcokuipers.pokemoncardcollection.dto.UserProfileUpdateDTO;
import nl.remcokuipers.pokemoncardcollection.entity.User;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import nl.remcokuipers.pokemoncardcollection.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping
    public ResponseEntity<UserProfileResponseDTO> addUserProfile(@Valid @RequestBody UserProfileRequestDTO userProfileRequestDTO) {
        User user = currentUserService.getCurrentUser();
        UserProfileResponseDTO createdEntry = userProfileService.addUserProfile(userProfileRequestDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }

    @PutMapping
    public ResponseEntity<UserProfileResponseDTO> updateUserProfile(@Valid @RequestBody UserProfileUpdateDTO dto) {
        User user = currentUserService.getCurrentUser();
        UserProfileResponseDTO updatedEntry = userProfileService.updateUserProfile(dto, user);
        return ResponseEntity.ok(updatedEntry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        User user = currentUserService.getCurrentUser();
        userProfileService.deleteUserProfile(id, user);
        return ResponseEntity.noContent().build();
    }
}
