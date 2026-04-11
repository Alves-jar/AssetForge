package com.noxus.AssetForge.service;

import com.noxus.AssetForge.dto.user.UserRequestDTO;
import com.noxus.AssetForge.dto.user.UserResponseDTO;
import com.noxus.AssetForge.mapper.UserMapper;
import com.noxus.AssetForge.model.User;
import com.noxus.AssetForge.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public UserResponseDTO create(UserRequestDTO newUser) {
        if (newUser == null) throw new RuntimeException("User cannot be null");

        User entity = mapper.toEntity(newUser);
        User saved = repository.save(entity);

        return mapper.toDTO(saved);
    }

    public List<UserResponseDTO> findAll() {
        return repository.findAll()
            .stream()
            .map(mapper::toDTO)
            .toList();
    }

    public UserResponseDTO findById(UUID id) {
        User user = repository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "User not found with id: " + id
            ));

        return mapper.toDTO(user);
    }

    public UserResponseDTO findByUsername(String username) {
        User user = repository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException(
                "No records found for this username!" + username
            ));
        return mapper.toDTO(user);
    }

    public UserResponseDTO update(UUID id, UserRequestDTO user) {
        if (user == null) throw new RuntimeException("User cannot be null");

        User entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "User not found with id: " + id
            ));

        entity.setUsername(user.username());
        entity.setEmail(user.email());

        if (user.password() != null && !user.password().isBlank()) {
            entity.setPasswordHash(user.password());
        }

        return mapper.toDTO(repository.save(entity));
    }

    public void delete(UUID id) {
        User user = repository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "User not found with id: " + id
            ));

        repository.delete(user);
    }
}