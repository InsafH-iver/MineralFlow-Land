package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.ZonedDateTime;

public record DeliveryTicketDto(String resourceName, ZonedDateTime deliveryTime,
                                int warehouseNumber) {
}
