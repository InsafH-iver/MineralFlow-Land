package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record TruckArrivalAtWarehouseDto(UUID vendorId, UUID resourceId,
                                         ZonedDateTime timestamp, String licensePlate) {
}
