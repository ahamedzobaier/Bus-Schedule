package com.uu.bus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uu.bus.model.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // You can add custom queries here later, like finding by category
}
