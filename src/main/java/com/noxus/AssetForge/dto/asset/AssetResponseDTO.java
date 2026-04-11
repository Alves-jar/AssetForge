package com.noxus.AssetForge.dto.asset;

import java.time.Instant;
import java.util.UUID;

public record AssetResponseDTO(
    UUID id,
    String name,
    Double price,
    UUID sellerId,
    String sellerName,
    Instant createdAt
) {}