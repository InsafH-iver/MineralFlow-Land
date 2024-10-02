package be.kdg.mineralflow.land.config;

import jakarta.annotation.PostConstruct;
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

    public int getCompanyTruckCapacity() {
        return truckCapacity;
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

    @PostConstruct
    public void init() {
        System.out.println("startOfPeriodWithAppointment = " + startOfPeriodWithAppointment);
        System.out.println("endOfPeriodWithAppointment = " + endOfPeriodWithAppointment);
        System.out.println("durationOfTimeslotOfAppointment = " + durationOfTimeslotOfAppointmentInMinutes);
        System.out.println("weighbridgeAmount = " + weighbridgeAmount);
        System.out.println("truckCapacity = " + truckCapacity);
    }
}
