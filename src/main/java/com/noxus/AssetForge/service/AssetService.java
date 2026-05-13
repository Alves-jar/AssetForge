package com.noxus.AssetForge.service;

import com.noxus.AssetForge.dto.asset.AssetRequestDTO;
import com.noxus.AssetForge.dto.asset.AssetResponseDTO;
import com.noxus.AssetForge.dto.asset.AssetUploadDTO;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
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

    public AssetResponseDTO create(AssetUploadDTO newAsset) {

        if (newAsset == null) {
            throw new RequiredObjectIsNullException(
                "Asset cannot be null"
            );
        }

        if (newAsset.file() == null || newAsset.file().isEmpty()) {
            throw new RuntimeException(
                "File is required"
            );
        }

        User seller = findSeller(newAsset.sellerId());

        String uploadDir = "uploads/assets/";

        String originalFilename = StringUtils.cleanPath(
            Objects.requireNonNull(
                newAsset.file().getOriginalFilename()
            )
        );

        String filename = UUID.randomUUID()
            + "_"
            + originalFilename;

        Path uploadPath = Paths.get(uploadDir);

        try {

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);

            Files.copy(
                newAsset.file().getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
            );

            Asset asset = new Asset();

            asset.setName(newAsset.name());
            asset.setPrice(newAsset.price());
            asset.setSeller(seller);

            asset.setAssetUrl(
                "/uploads/assets/" + filename
            );

            Asset saved = repository.save(asset);

            return mapper.toDTO(saved);

        } catch (IOException e) {

            throw new RuntimeException(
                "Error uploading file",
                e
            );
        }
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