package be.kdg.mineralflow.land.business.util.response;

import java.time.ZonedDateTime;
import java.util.UUID;

public record DeliveryDataResponse(UUID vendorId, UUID resourceId,
                                   ZonedDateTime deliveryTime, UUID unloadingRequestId) {
}
