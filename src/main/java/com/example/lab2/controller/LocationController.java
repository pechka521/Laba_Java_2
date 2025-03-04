package com.example.lab2.controller;

import com.example.lab2.model.Location;
import com.example.lab2.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class LocationController {

    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<Location>> getAll() {
        logger.info("Getting all locations");
        return ResponseEntity.ok(locationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getById(@PathVariable Long id) {
        logger.info("Getting location by ID: {}", id);
        return locationService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Location> create(
            @RequestBody Location location,
            @RequestParam(required = false) List<Long> sunriseSunsetIds) {
        logger.info("Creating location: {}", location);
        Location created = locationService.create(location, sunriseSunsetIds);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> update(
            @PathVariable Long id,
            @RequestBody Location location,
            @RequestParam(required = false) List<Long> sunriseSunsetIds) {
        logger.info("Updating location ID: {}", id);
        return locationService.update(id, location, sunriseSunsetIds)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting location ID: {}", id);
        boolean deleted = locationService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}