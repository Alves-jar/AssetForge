package com.noxus.AssetForge.controller;

import com.noxus.AssetForge.dto.UserRequestDTO;
import com.noxus.AssetForge.dto.UserResponseDTO;
import com.noxus.AssetForge.model.User;
import com.noxus.AssetForge.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponseDTO create(@RequestBody UserRequestDTO newUser) {
        return service.create(newUser);
    }

    @GetMapping
    public List<UserResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{username}")
    public UserResponseDTO findByUsername(@PathVariable("username") String username) {
        return service.findByUsername(username);
    }
}
