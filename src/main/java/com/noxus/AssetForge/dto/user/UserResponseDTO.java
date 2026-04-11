package com.noxus.AssetForge.dto.user;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String username,
    String email,
    boolean isSeller,
    Instant createdAt
) {
}
