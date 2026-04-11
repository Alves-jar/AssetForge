package com.noxus.AssetForge.service;

import com.noxus.AssetForge.dto.UserRequestDTO;
import com.noxus.AssetForge.dto.UserResponseDTO;
import com.noxus.AssetForge.mapper.UserMapper;
import com.noxus.AssetForge.model.User;
import com.noxus.AssetForge.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public UserResponseDTO findByUsername(String username) {
        User user = repository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("No records found for this username!" + username));
        return mapper.toDTO(user);
    }

    public UserResponseDTO update(UserRequestDTO user) {
        if (user == null) throw new RuntimeException("User cannot be null");

        User entity = repository.findByUsername(user.username())
            .orElseThrow(() -> new RuntimeException("No records found for this username!" + user.username()));

        return mapper.toDTO(entity);
    }

    public void delete(String username) {
        User user = repository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("No records found for this username!" + username));

        repository.delete(user);
    }
}