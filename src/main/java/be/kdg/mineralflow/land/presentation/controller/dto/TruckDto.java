package be.kdg.mineralflow.land.presentation.controller.dto;

public class TruckDto {
    private final String licensePlate;
    private final VisitDto visit;

    public TruckDto(String licensePlate, VisitDto visit) {
        this.licensePlate = licensePlate;
        this.visit = visit;
    }
}
