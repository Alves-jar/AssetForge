package com.noxus.AssetForge.mapper;

import com.noxus.AssetForge.dto.user.UserRequestDTO;
import com.noxus.AssetForge.dto.user.UserResponseDTO;
import com.noxus.AssetForge.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserResponseDTO, UserRequestDTO> {

    @Override
    public UserResponseDTO toDTO(User user) {
        if (user == null) return null;

        return new UserResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.isSeller(),
            user.getCreatedAt()
        );
    }

    @Override
    public User toEntity(UserRequestDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPasswordHash(dto.password());

        return user;
    }
}
