package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record StockPortionDropDto(UUID vendorId, UUID resourceId,
                                  double weight , ZonedDateTime endWeightTime,UUID unloadingRequestId) {
}
