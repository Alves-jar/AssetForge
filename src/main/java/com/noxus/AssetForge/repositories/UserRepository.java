package com.noxus.AssetForge.repositories;

import com.noxus.AssetForge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
