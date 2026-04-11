package com.noxus.AssetForge.mapper;

import com.noxus.AssetForge.dto.asset.AssetRequestDTO;
import com.noxus.AssetForge.dto.asset.AssetResponseDTO;
import com.noxus.AssetForge.model.Asset;
import org.springframework.stereotype.Component;

@Component
public class AssetMapper implements Mapper<Asset, AssetResponseDTO, AssetRequestDTO> {

    @Override
    public AssetResponseDTO toDTO(Asset asset) {
        if (asset == null) return null;

        return new AssetResponseDTO(
            asset.getId(),
            asset.getName(),
            asset.getPrice(),
            asset.getSeller().getId(),
            asset.getSeller().getUsername(),
            asset.getCreatedAt()
        );
    }

    @Override
    public Asset toEntity(AssetRequestDTO dto) {
        if (dto == null) return null;

        Asset asset = new Asset();
        asset.setName(dto.name());
        asset.setPrice(dto.price());

        return asset;
    }
}