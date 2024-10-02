package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.ZonedDateTime;

public class VisitDto {
    private final ZonedDateTime arrivalTime;
    private final ZonedDateTime leavingTIme;
    private final WeighbridgeDto weighbridge;

    public VisitDto(ZonedDateTime arrivalTime, ZonedDateTime leavingTIme, WeighbridgeDto weighbridge) {
        this.arrivalTime = arrivalTime;
        this.leavingTIme = leavingTIme;
        this.weighbridge = weighbridge;
    }
}
