package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.ZonedDateTime;

public record MakeAppointmentDto(String vendorName, String resourceName, ZonedDateTime appointmentDate,
                                 String licensePlate) {}
