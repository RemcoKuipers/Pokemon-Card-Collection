package nl.remcokuipers.pokemoncardcollection.controller;

import jakarta.validation.Valid;
import nl.remcokuipers.pokemoncardcollection.dto.UserEnabledUpdateDTO;
import nl.remcokuipers.pokemoncardcollection.dto.UserResponseDTO;
import nl.remcokuipers.pokemoncardcollection.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}/enabled")
    public ResponseEntity<UserResponseDTO> updateUserEnabled(
            @PathVariable long id,
            @Valid @RequestBody UserEnabledUpdateDTO dto
    ) {
        return ResponseEntity.ok(userService.updateUserEnabled(id, dto));
    }
}
