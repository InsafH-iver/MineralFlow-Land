package be.kdg.mineralflow.land.presentation.controller.dto;

import java.util.List;

public record PlanningDto(int queueSize, List<AppointmentDto> appointments) {
}
