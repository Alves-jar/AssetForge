package com.noxus.AssetForge.dto.asset;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record AssetUploadDTO(
    String name,
    Double price,
    UUID sellerId,
    MultipartFile file
) {}