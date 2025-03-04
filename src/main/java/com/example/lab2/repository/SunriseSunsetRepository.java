package com.example.lab2.repository;

import com.example.lab2.model.SunriseSunset;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SunriseSunsetRepository extends JpaRepository<SunriseSunset, Long> {

    @EntityGraph(attributePaths = {"locations"})
    List<SunriseSunset> findAll();

    @EntityGraph(attributePaths = {"locations"})
    Optional<SunriseSunset> findById(Long id);
}