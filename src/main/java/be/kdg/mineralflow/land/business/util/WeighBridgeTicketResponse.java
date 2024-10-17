package be.kdg.mineralflow.land.business.util;

import java.time.ZonedDateTime;

public record WeighBridgeTicketResponse(double startWeightAmountInTon,
                                        ZonedDateTime startWeightTimestamp, double endWeightAmountInTon,
                                        ZonedDateTime endWeightTimestamp, String licensePlate) implements WeighingResponse {
}
