package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.ZonedDateTime;

public record TimeslotDto(ZonedDateTime startOfTimeslot, ZonedDateTime endOfTimeslot) {
}
