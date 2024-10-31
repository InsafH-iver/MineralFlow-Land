package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.ZonedDateTime;

public record DeliveryTicketInputDto(String licensePlate, ZonedDateTime timestamp) {
}
