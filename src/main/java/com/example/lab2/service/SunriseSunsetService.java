package com.example.lab2.service;

import com.example.lab2.model.Location;
import com.example.lab2.model.SunriseSunset;
import com.example.lab2.repository.LocationRepository;
import com.example.lab2.repository.SunriseSunsetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SunriseSunsetService {

    private static final String API_URL = "https://api.sunrise-sunset.org/json";
    private final SunriseSunsetRepository repository;
    private final LocationRepository locationRepository;
    private final RestTemplate restTemplate;

    // Получение данных с API и сохранение
    @Transactional
    public Map<String, Object> getSunriseSunset(double latitude, double longitude, String date, List<Long> locationIds) {
        String url = String.format("%s?lat=%f&lng=%f&date=%s",
                API_URL, latitude, longitude, date != null ? date : "today");

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || !"OK".equals(response.get("status"))) {
            throw new RuntimeException("Failed to fetch sunrise/sunset data");
        }

        SunriseSunset sunriseSunset = new SunriseSunset();
        sunriseSunset.setLatitude(latitude);
        sunriseSunset.setLongitude(longitude);
        sunriseSunset.setDate(date != null ? date : "today");

        Map<String, Object> results = (Map<String, Object>) response.get("results");
        sunriseSunset.setSunrise((String) results.get("sunrise"));
        sunriseSunset.setSunset((String) results.get("sunset"));

        // Связываем с местоположениями
        if (locationIds != null && !locationIds.isEmpty()) {
            List<Location> locations = locationRepository.findAllById(locationIds);
            sunriseSunset.getLocations().addAll(locations);
        }

        repository.save(sunriseSunset);
        return response;
    }

    // CRUD операции
    @Transactional(readOnly = true)
    public List<SunriseSunset> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<SunriseSunset> getById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public SunriseSunset create(SunriseSunset sunriseSunset, List<Long> locationIds) {
        if (locationIds != null && !locationIds.isEmpty()) {
            List<Location> locations = locationRepository.findAllById(locationIds);
            sunriseSunset.getLocations().addAll(locations);
        }
        return repository.save(sunriseSunset);
    }

    @Transactional
    public Optional<SunriseSunset> update(Long id, SunriseSunset updatedData, List<Long> locationIds) {
        return repository.findById(id).map(sunriseSunset -> {
            sunriseSunset.setLatitude(updatedData.getLatitude());
            sunriseSunset.setLongitude(updatedData.getLongitude());
            sunriseSunset.setDate(updatedData.getDate());
            sunriseSunset.setSunrise(updatedData.getSunrise());
            sunriseSunset.setSunset(updatedData.getSunset());

            if (locationIds != null) {
                sunriseSunset.getLocations().clear();
                List<Location> locations = locationRepository.findAllById(locationIds);
                sunriseSunset.getLocations().addAll(locations);
            }
            return repository.save(sunriseSunset);
        });
    }

    @Transactional
    public boolean delete(Long id) {
        return repository.findById(id).map(sunriseSunset -> {
            repository.delete(sunriseSunset);
            return true;
        }).orElse(false);
    }
}