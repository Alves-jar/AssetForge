package com.noxus.AssetForge.service;

import com.noxus.AssetForge.dto.response.PageResponse;
import com.noxus.AssetForge.dto.user.UserRequestDTO;
import com.noxus.AssetForge.dto.user.UserResponseDTO;
import com.noxus.AssetForge.exception.RequiredObjectIsNullException;
import com.noxus.AssetForge.exception.ResourceNotFoundException;
import com.noxus.AssetForge.mapper.UserMapper;
import com.noxus.AssetForge.model.User;
import com.noxus.AssetForge.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        if (newUser == null) throw new RequiredObjectIsNullException("User cannot be null");

        User entity = mapper.toEntity(newUser);
        User saved = repository.save(entity);

        return mapper.toDTO(saved);
    }

    public PageResponse<UserResponseDTO> findAll(Pageable pageable) {
        Page<User> page = repository.findAll(pageable);

        return buildPageResponse(page);
    }

    public UserResponseDTO findById(UUID id) {
        User user = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with id: " + id
            ));

        return mapper.toDTO(user);
    }

    public UserResponseDTO findByUsername(String username) {
        User user = repository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No records found for this username: " + username
            ));

        return mapper.toDTO(user);
    }

    public UserResponseDTO update(UUID id, UserRequestDTO user) {
        if (user == null) throw new RequiredObjectIsNullException("User cannot be null");

        User entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
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
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with id: " + id
            ));

        repository.delete(user);
    }

    private PageResponse<UserResponseDTO> buildPageResponse(Page<User> page) {
        return new PageResponse<>(
            page.getContent().stream().map(mapper::toDTO).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}