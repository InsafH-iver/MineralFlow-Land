package be.kdg.mineralflow.land.business.util;

import java.time.ZonedDateTime;

public record TruckArrivalResponse(boolean gateStatus, ZonedDateTime returnTimeOfTruck) {
}
