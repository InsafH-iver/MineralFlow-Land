package be.kdg.mineralflow.land.business.domain;

import jakarta.persistence.Entity;

import java.time.ZonedDateTime;

@Entity
public class UnloadingWithoutAppointment extends UnloadingRequest {
    private ZonedDateTime createdAt;

    public UnloadingWithoutAppointment(String licensePlate, ZonedDateTime createdAt) {
        super(licensePlate);
        this.createdAt = createdAt;
    }

    protected UnloadingWithoutAppointment() {
    }
}
