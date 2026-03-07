package com.uu.bus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uu.bus.model.SupportContact;

@Repository
public interface SupportContactRepository extends JpaRepository<SupportContact, Long> {
}
