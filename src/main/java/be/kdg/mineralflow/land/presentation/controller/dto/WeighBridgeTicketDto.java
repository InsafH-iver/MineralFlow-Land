package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.ZonedDateTime;

public record WeighBridgeTicketDto(String licensePlate, double weight, ZonedDateTime timestamp) {
}
