package com.uu.bus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uu.bus.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}