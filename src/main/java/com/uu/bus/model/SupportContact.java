package com.uu.bus.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "support_contacts")
public class SupportContact {

    @Id
    private Long id = 1L; // Lock to ID 1 so we only ever have one "Office" record

    private String hotlineNumber;
    private String officeEmail;
    private String officeLocation;
    private String officeHours;

    // Default constructor for JPA
    public SupportContact() {
    }

    // Convenience constructor for initial setup
    public SupportContact(Long id, String hotlineNumber, String officeEmail, String officeLocation, String officeHours) {
        this.id = id;
        this.hotlineNumber = hotlineNumber;
        this.officeEmail = officeEmail;
        this.officeLocation = officeLocation;
        this.officeHours = officeHours;
    }
}
