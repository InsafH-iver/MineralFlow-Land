package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.ZonedDateTime;

public record TruckArrivalDto(boolean open, ZonedDateTime returningDate) {
}
