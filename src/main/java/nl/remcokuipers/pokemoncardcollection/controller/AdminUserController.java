package nl.remcokuipers.pokemoncardcollection.controller;

import jakarta.validation.Valid;
import nl.remcokuipers.pokemoncardcollection.dto.UserEnabledUpdateDTO;
import nl.remcokuipers.pokemoncardcollection.dto.UserResponseDTO;
import nl.remcokuipers.pokemoncardcollection.service.CurrentUserService;
import nl.remcokuipers.pokemoncardcollection.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;
    private final CurrentUserService currentUserService;

    public AdminUserController(UserService userService, CurrentUserService currentUserService) {
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        currentUserService.getCurrentUser();
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long id) {
        currentUserService.getCurrentUser();
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}/enabled")
    public ResponseEntity<UserResponseDTO> updateUserEnabled(
            @PathVariable long id,
            @Valid @RequestBody UserEnabledUpdateDTO dto
    ) {
        currentUserService.getCurrentUser();
        return ResponseEntity.ok(userService.updateUserEnabled(id, dto));
    }
}
