package com.noxus.AssetForge.dto;

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
