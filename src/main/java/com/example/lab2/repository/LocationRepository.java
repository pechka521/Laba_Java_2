package com.example.lab2.repository;

import com.example.lab2.model.Location;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    @EntityGraph(attributePaths = {"sunriseSunsets"})
    List<Location> findAll();

    @EntityGraph(attributePaths = {"sunriseSunsets"})
    Optional<Location> findById(Long id);
}