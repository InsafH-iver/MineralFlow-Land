package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.ZonedDateTime;

public record VisitDto(ZonedDateTime arrivalTime, ZonedDateTime leavingTime, WeighbridgeDto weighbridge) {
}
