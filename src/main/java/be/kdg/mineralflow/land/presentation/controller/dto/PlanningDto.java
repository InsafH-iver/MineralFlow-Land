package be.kdg.mineralflow.land.presentation.controller.dto;

import java.util.List;

public record PlanningDto(List<TruckDto> queue, List<AppointmentDto> appointments) {
}
