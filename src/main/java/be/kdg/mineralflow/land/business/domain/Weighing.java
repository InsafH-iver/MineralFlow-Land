package be.kdg.mineralflow.land.business.domain;

import java.time.ZonedDateTime;

public class Weighing {
    private double amountInTon;
    private ZonedDateTime timestamp;

    public Weighing(double amountInTon, ZonedDateTime timestamp) {
        this.amountInTon = amountInTon;
        this.timestamp = timestamp;
    }

    protected Weighing() {}

    public double getAmountInTon() {
        return amountInTon;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
