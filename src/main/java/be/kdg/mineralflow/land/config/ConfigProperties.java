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
    private int truckCapacityDuringAppointments;
    private int truckCapacityDuringQueue;
    private String warehouseBaseUrl;
    private String warehouseNumberUrl;
    private String warehouseCapacityBaseUrl;
    private String warehouseCapacityIsFullUrl;

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

    public void setDurationOfTimeslotOfAppointmentInMinutes(int durationOfTimeslotOfAppointmentInMinutes) {
        this.durationOfTimeslotOfAppointmentInMinutes = durationOfTimeslotOfAppointmentInMinutes;
    }

    public void setEndOfPeriodWithAppointment(int endOfPeriodWithAppointment) {
        this.endOfPeriodWithAppointment = endOfPeriodWithAppointment;
    }

    public void setStartOfPeriodWithAppointment(int startOfPeriodWithAppointment) {
        this.startOfPeriodWithAppointment = startOfPeriodWithAppointment;
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

    public String getWarehouseCapacityIsFullUrl() {
        return warehouseCapacityIsFullUrl;
    }

    public void setWarehouseCapacityIsFullUrl(String warehouseCapacityIsFullUrl) {
        this.warehouseCapacityIsFullUrl = warehouseCapacityIsFullUrl;
    }

    public String getWarehouseCapacityBaseUrl() {
        return warehouseCapacityBaseUrl;
    }

    public void setWarehouseCapacityBaseUrl(String warehouseCapacityBaseUrl) {
        this.warehouseCapacityBaseUrl = warehouseCapacityBaseUrl;
    }

    public int getTruckCapacityDuringAppointments() {
        return truckCapacityDuringAppointments;
    }

    public void setTruckCapacityDuringAppointments(int truckCapacityDuringAppointments) {
        this.truckCapacityDuringAppointments = truckCapacityDuringAppointments;
    }

    public int getTruckCapacityDuringQueue() {
        return truckCapacityDuringQueue;
    }

    public void setTruckCapacityDuringQueue(int truckCapacityDuringQueue) {
        this.truckCapacityDuringQueue = truckCapacityDuringQueue;
    }
}
