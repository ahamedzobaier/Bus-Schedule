package com.uu.bus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uu.bus.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
}