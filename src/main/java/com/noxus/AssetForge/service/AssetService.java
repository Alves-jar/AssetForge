package com.noxus.AssetForge.service;

import com.noxus.AssetForge.dto.asset.AssetRequestDTO;
import com.noxus.AssetForge.dto.asset.AssetResponseDTO;
import com.noxus.AssetForge.dto.response.PageResponse;
import com.noxus.AssetForge.exception.RequiredObjectIsNullException;
import com.noxus.AssetForge.exception.ResourceNotFoundException;
import com.noxus.AssetForge.mapper.AssetMapper;
import com.noxus.AssetForge.model.Asset;
import com.noxus.AssetForge.model.User;
import com.noxus.AssetForge.repositories.AssetRepository;
import com.noxus.AssetForge.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AssetService {

    private final AssetRepository repository;
    private final AssetMapper mapper;
    private final UserRepository userRepository;

    public AssetService(AssetRepository repository, AssetMapper mapper, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    public AssetResponseDTO create(AssetRequestDTO newAsset) {
        if (newAsset == null) throw new RequiredObjectIsNullException("Asset cannot be null");

        User seller = findSeller(newAsset.sellerId());
        Asset entity = mapper.toEntity(newAsset);
        entity.setSeller(seller);
        Asset saved = repository.save(entity);

        return mapper.toDTO(saved);
    }

    public PageResponse<AssetResponseDTO> findAll(Pageable pageable) {
        Page<Asset> page = repository.findAll(pageable);

        return buildPageResponse(page);
    }

    public AssetResponseDTO findById(UUID id) {
        Asset asset = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Asset not found with id: " + id
            ));

        return mapper.toDTO(asset);
    }

    public AssetResponseDTO findByName(String name) {
        Asset asset = repository.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException(
                "No records found for this name: " + name
            ));

        return mapper.toDTO(asset);
    }

    public AssetResponseDTO update(UUID id, AssetRequestDTO asset) {
        if (asset == null)
            throw new RequiredObjectIsNullException("Asset cannot be null");

        Asset entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Asset not found with id: " + id
            ));

        entity.setName(asset.name());
        entity.setPrice(asset.price());

        if (asset.sellerId() != null &&
            !asset.sellerId().equals(entity.getSeller().getId())) {

            User seller = findSeller(asset.sellerId());
            entity.setSeller(seller);
        }

        Asset updated = repository.save(entity);

        return mapper.toDTO(updated);
    }

    public void delete(UUID id) {
        Asset asset = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Asset not found with id: " + id
            ));

        repository.delete(asset);
    }

    private User findSeller(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Seller not found with id: " + id
            ));
    }

    private PageResponse<AssetResponseDTO> buildPageResponse(Page<Asset> page) {
        return new PageResponse<>(
            page.getContent().stream().map(mapper::toDTO).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages()
        );
    }
}