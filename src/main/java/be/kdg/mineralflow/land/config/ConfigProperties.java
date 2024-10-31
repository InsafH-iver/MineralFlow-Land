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
    private String warehouseHostAddress;
    private String warehouseRestUrl;
    private String warehouseCapacityRestUrl;
    private String deliveryTicketRestUrl;
    private String warehouseCapacityIsFullUrl;
    private String warehouseNumberUrl;

    public String getWarehouseNumberUrl() {
        return warehouseNumberUrl;
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

    public void setWarehouseNumberUrl(String warehouseNumberUrl) {
        this.warehouseNumberUrl = warehouseNumberUrl;
    }

    public String getWarehouseCapacityIsFullUrl() {
        return warehouseCapacityIsFullUrl;
    }

    public void setWarehouseCapacityIsFullUrl(String warehouseCapacityIsFullUrl) {
        this.warehouseCapacityIsFullUrl = warehouseCapacityIsFullUrl;
    }

    public String getWarehouseHostAddress() {
        return warehouseHostAddress;
    }

    public void setWarehouseHostAddress(String warehouseHostAddress) {
        this.warehouseHostAddress = warehouseHostAddress;
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

    public String getWarehouseRestUrl() {
        return warehouseRestUrl;
    }

    public void setWarehouseRestUrl(String warehouseRestUrl) {
        this.warehouseRestUrl = warehouseRestUrl;
    }

    public String getWarehouseCapacityRestUrl() {
        return warehouseCapacityRestUrl;
    }

    public void setWarehouseCapacityRestUrl(String warehouseCapacityRestUrl) {
        this.warehouseCapacityRestUrl = warehouseCapacityRestUrl;
    }

    public String getDeliveryTicketRestUrl() {
        return deliveryTicketRestUrl;
    }

    public void setDeliveryTicketRestUrl(String deliveryTicketRestUrl) {
        this.deliveryTicketRestUrl = deliveryTicketRestUrl;
    }
}
