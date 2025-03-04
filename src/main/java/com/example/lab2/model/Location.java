package com.example.lab2.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "location")
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    @ManyToMany(mappedBy = "locations", fetch = FetchType.LAZY)
    private Set<SunriseSunset> sunriseSunsets = new HashSet<>();
}