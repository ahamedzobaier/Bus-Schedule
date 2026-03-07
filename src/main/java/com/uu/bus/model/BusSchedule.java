package com.uu.bus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bus_schedules")
public class BusSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String busNumber;
    private String startTime;
    private String route;
    private String arrivalTime;
    private String driverName;
    private String contact;
    private String dayOfWeek; // Saturday, Sunday, etc.
    private String direction; // UP or DOWN

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    // Getters and Setters (Standard)
    // ... your existing variables ...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // Inside BusSchedule.java
    private String liveTrackingUrl;

    public String getLiveTrackingUrl() {
        return liveTrackingUrl;
    }

    public void setLiveTrackingUrl(String liveTrackingUrl) {
        this.liveTrackingUrl = liveTrackingUrl;
    }

    // Make sure you have these for the other fields too so the Admin Controller works:
    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
