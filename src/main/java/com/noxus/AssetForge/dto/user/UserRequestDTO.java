package com.noxus.AssetForge.dto.user;

public record UserRequestDTO(
    String username,
    String email,
    String password
) {
}
