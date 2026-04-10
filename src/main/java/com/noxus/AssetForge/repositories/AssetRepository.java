package com.noxus.AssetForge.repositories;

import com.noxus.AssetForge.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssetRepository extends JpaRepository<Asset, UUID> {
}
