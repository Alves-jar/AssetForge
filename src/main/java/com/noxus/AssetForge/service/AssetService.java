package com.noxus.AssetForge.service;

import com.noxus.AssetForge.dto.asset.AssetRequestDTO;
import com.noxus.AssetForge.dto.asset.AssetResponseDTO;
import com.noxus.AssetForge.mapper.AssetMapper;
import com.noxus.AssetForge.model.Asset;
import com.noxus.AssetForge.model.User;
import com.noxus.AssetForge.repositories.AssetRepository;
import com.noxus.AssetForge.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
        if (newAsset == null) throw new RuntimeException("Asset cannot be null");

        User seller = findSeller(newAsset.sellerId());
        Asset entity = mapper.toEntity(newAsset);
        entity.setSeller(seller);
        Asset saved = repository.save(entity);

        return mapper.toDTO(saved);
    }

    public List<AssetResponseDTO> findAll() {
        return repository.findAll()
            .stream()
            .map(mapper::toDTO)
            .toList();
    }

    public AssetResponseDTO findById(UUID id) {
        Asset asset = repository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Asset not found with id: " + id
            ));

        return mapper.toDTO(asset);
    }

    public AssetResponseDTO findByName(String name) {
        Asset asset = repository.findByName(name)
            .orElseThrow(() -> new RuntimeException(
                "No records found for this name!" + name
            ));
        return mapper.toDTO(asset);
    }

    public AssetResponseDTO update(UUID id, AssetRequestDTO asset) {
        if (asset == null) {
            throw new RuntimeException("Asset cannot be null");
        }

        Asset entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Asset not found with id: " + id
            ));

        entity.setName(asset.name());
        entity.setPrice(asset.price());

        if (asset.sellerId() != null) {
            User seller = findSeller(asset.sellerId());
            entity.setSeller(seller);
        }

        Asset updated = repository.save(entity);

        return mapper.toDTO(updated);
    }

    public void delete(UUID id) {
        Asset asset = repository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                "Asset not found with id: " + id
            ));

        repository.delete(asset);
    }

    private User findSeller(UUID id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Seller not found with id: " + id));
    }
}