package be.kdg.mineralflow.land.business.domain;

import be.kdg.mineralflow.land.business.util.WeighBridgeTicketResponse;
import be.kdg.mineralflow.land.exception.ProcessAlreadyFulfilledException;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
public class WeighbridgeTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountInTon", column = @Column(name = "start_weight_amount_in_ton")),
            @AttributeOverride(name = "timestamp", column = @Column(name = "start_weight_timestamp"))
    })
    private Weighing startWeight;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountInTon", column = @Column(name = "end_weight_amount_in_ton")),
            @AttributeOverride(name = "timestamp", column = @Column(name = "end_weight_timestamp"))
    })
    private Weighing endWeight;

    protected WeighbridgeTicket() {
    }

    public WeighbridgeTicket(double startWeightAmountInTon,
                             ZonedDateTime startWeightTimestamp) {
        startWeight = new Weighing(startWeightAmountInTon, startWeightTimestamp);
    }

    public void updateEndWeight(double endWeightAmountInTon, ZonedDateTime endWeightTimestamp) {
        if (endWeight != null) {
            throw new ProcessAlreadyFulfilledException("WeightbridgeTicket already has an end weight");
        }
        endWeight = new Weighing(endWeightAmountInTon, endWeightTimestamp);
    }

    public WeighBridgeTicketResponse getWeighBridgeTicketData(String licensePlate) {
        return new WeighBridgeTicketResponse(startWeight.getAmountInTon(), startWeight.getTimestamp(),
                endWeight.getAmountInTon(), endWeight.getTimestamp(), licensePlate);
    }

    public double getNetWeight() {
        if (startWeight == null || endWeight == null) {
            throw new IllegalStateException("WeighbridgeTicket has not been fulfilled yet");
        }
        return startWeight.getAmountInTon() - endWeight.getAmountInTon();
    }

    public Weighing getEndWeight() {
        return endWeight;
    }

    public Weighing getStartWeight() {
        return startWeight;
    }
}
