package be.kdg.mineralflow.land.business.domain;

import be.kdg.mineralflow.land.business.util.response.WeighBridgeTicketResponse;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

@Entity
public class Visit {
    public static final Logger logger = Logger
            .getLogger(Visit.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID Id;
    private ZonedDateTime arrivalTime;
    private ZonedDateTime leavingTime;

    @ManyToOne
    private Weighbridge weighBridge;
    @OneToOne(cascade = CascadeType.ALL)
    private WeighbridgeTicket weighbridgeTicket;

    public Visit(ZonedDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    protected Visit() {
    }

    public void setWeighbridgeTicket(double startWeightAmountInTon,
                                     ZonedDateTime startWeightTimestamp) {
        this.weighbridgeTicket =
                new WeighbridgeTicket(startWeightAmountInTon, startWeightTimestamp);
    }

    public void updateEndWeightOfWeighbridgeTicket(double endWeightAmountInTon,
                                                   ZonedDateTime endWeightTimestamp) {
        weighbridgeTicket.updateEndWeight(endWeightAmountInTon, endWeightTimestamp);
    }

    public ZonedDateTime getArrivalTime() {
        logger.info(String.format("Fetching arrival  time %s from Visit",
                arrivalTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)));
        return arrivalTime;
    }

    public boolean hasWeighbridgeTicket() {
        return weighbridgeTicket != null;
    }

    public double getNetWeightOfWeighBridgeTicket() {
        return weighbridgeTicket.getNetWeight();
    }

    public WeighBridgeTicketResponse getWeighBridgeTicketData(String licensePlate) {
        return weighbridgeTicket.getWeighBridgeTicketData(licensePlate);
    }

    public WeighbridgeTicket getWeighbridgeTicket() {
        return weighbridgeTicket;
    }

    public Weighbridge getWeighBridge() {
        return weighBridge;
    }

    public ZonedDateTime getLeavingTime() {
        return leavingTime;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "Id=" + Id +
                ", arrivalTime=" + arrivalTime +
                ", leavingTime=" + leavingTime +
                ", weighBridge=" + weighBridge +
                ", weighbridgeTicket=" + weighbridgeTicket +
                '}';
    }


    public void setTimeOfDeparture(ZonedDateTime timeOfDeparture) {
        this.leavingTime = timeOfDeparture;
    }
}
