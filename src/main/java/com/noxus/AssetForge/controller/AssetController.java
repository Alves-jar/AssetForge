package com.noxus.AssetForge.controller;

import com.noxus.AssetForge.dto.asset.AssetRequestDTO;
import com.noxus.AssetForge.dto.asset.AssetResponseDTO;
import com.noxus.AssetForge.service.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assets")
public class AssetController {

    private final AssetService service;

    public AssetController(AssetService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AssetResponseDTO> create(@RequestBody AssetRequestDTO newAsset) {
        AssetResponseDTO created = service.create(newAsset);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(created.id())
            .toUri();

        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping()
    public ResponseEntity<List<AssetResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AssetResponseDTO> findByName(@PathVariable String name) {

        return ResponseEntity.ok(service.findByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponseDTO> update(
        @PathVariable UUID id,
        @RequestBody AssetRequestDTO asset) {

        return ResponseEntity.ok(service.update(id, asset));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}