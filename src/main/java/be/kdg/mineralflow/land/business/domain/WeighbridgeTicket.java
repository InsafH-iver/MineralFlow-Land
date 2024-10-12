package be.kdg.mineralflow.land.business.domain;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
public class WeighbridgeTicket {
    @Id
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

    public WeighbridgeTicket(double startWeightAmountInTon,
                             ZonedDateTime startWeightTimestamp ) {
        startWeight = new Weighing(startWeightAmountInTon, startWeightTimestamp);
    }
}
