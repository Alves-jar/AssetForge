package com.noxus.AssetForge.dto.asset;

import java.util.UUID;

public record AssetRequestDTO(
    String name,
    Double price,
    UUID sellerId
) {}
