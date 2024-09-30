package be.kdg.mineralflow.land.presentation.controller.dto;

import be.kdg.mineralflow.land.business.domain.Weighbridge;

public class WeighbridgeDto {
    private int weighbridgeNumber;

    public WeighbridgeDto(Weighbridge weighbridge) {
        this.weighbridgeNumber = weighbridge.getWeighbridgeNumber();
    }
}
