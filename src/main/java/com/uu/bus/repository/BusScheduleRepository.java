package com.uu.bus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uu.bus.model.BusSchedule;

public interface BusScheduleRepository extends JpaRepository<BusSchedule, Long> {
    List<BusSchedule> findByLocationNameAndDayOfWeekAndDirection(String locationName, String day, String direction);
}