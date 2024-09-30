package be.kdg.mineralflow.land.business.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Weighbridge {
    @Id
    private UUID id;
    private int weighbridgeNumber;

    protected Weighbridge() {
    }

    public Weighbridge(int weighbridgeNumber) {
        this.weighbridgeNumber = weighbridgeNumber;
    }

    public int getWeighbridgeNumber() {
        return weighbridgeNumber;
    }
}
