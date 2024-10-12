package be.kdg.mineralflow.land.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "company")
public class ConfigProperties {

    private int startOfPeriodWithAppointment;
    private int endOfPeriodWithAppointment;
    private int durationOfTimeslotOfAppointmentInMinutes;
    private int weighbridgeAmount;
    private int truckCapacity;
    private String warehouseBaseUrl;
    private String warehouseNumberUrl;

    public String getWarehouseNumberUrl() {
        return warehouseNumberUrl;
    }

    public String getWarehouseBaseUrl() {
        return warehouseBaseUrl;
    }

    public int getDurationOfTimeslotOfAppointmentInMinutes() {
        return durationOfTimeslotOfAppointmentInMinutes;
    }

    public int getEndOfPeriodWithAppointment() {
        return endOfPeriodWithAppointment;
    }

    public int getStartOfPeriodWithAppointment() {
        return startOfPeriodWithAppointment;
    }

    public int getWeighbridgeAmount() {
        return weighbridgeAmount;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public void setDurationOfTimeslotOfAppointmentInMinutes(int durationOfTimeslotOfAppointmentInMinutes) {
        this.durationOfTimeslotOfAppointmentInMinutes = durationOfTimeslotOfAppointmentInMinutes;
    }

    public void setEndOfPeriodWithAppointment(int endOfPeriodWithAppointment) {
        this.endOfPeriodWithAppointment = endOfPeriodWithAppointment;
    }

    public void setStartOfPeriodWithAppointment(int startOfPeriodWithAppointment) {
        this.startOfPeriodWithAppointment = startOfPeriodWithAppointment;
    }


    public void setTruckCapacity(int truckCapacity) {
        this.truckCapacity = truckCapacity;
    }

    public void setWeighbridgeAmount(int weighbridgeAmount) {
        this.weighbridgeAmount = weighbridgeAmount;
    }

    public void setWarehouseBaseUrl(String warehouseBaseUrl) {
        this.warehouseBaseUrl = warehouseBaseUrl;
    }

    public void setWarehouseNumberUrl(String warehouseNumberUrl) {
        this.warehouseNumberUrl = warehouseNumberUrl;
    }
}
