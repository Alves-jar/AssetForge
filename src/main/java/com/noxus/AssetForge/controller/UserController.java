package com.noxus.AssetForge.controller;

import com.noxus.AssetForge.dto.user.UserRequestDTO;
import com.noxus.AssetForge.dto.user.UserResponseDTO;
import com.noxus.AssetForge.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO newUser) {
        UserResponseDTO created = service.create(newUser);

        return ResponseEntity
            .created(URI.create("/users/" + created.id()))
            .body(created);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> findByUsername(
        @PathVariable String username) {

        return ResponseEntity.ok(service.findByUsername(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
        @PathVariable UUID id,
        @RequestBody UserRequestDTO user) {

        return ResponseEntity.ok(service.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}