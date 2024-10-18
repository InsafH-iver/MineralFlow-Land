package be.kdg.mineralflow.land.presentation.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

public final class AppointmentFormDataDto {
    private String vendorName;
    private String resourceName;
    private ZonedDateTime appointmentDate;
    private String licensePlate;

    public AppointmentFormDataDto() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AppointmentFormDataDto) obj;
        return Objects.equals(this.vendorName, that.vendorName) &&
                Objects.equals(this.resourceName, that.resourceName) &&
                Objects.equals(this.appointmentDate, that.appointmentDate) &&
                Objects.equals(this.licensePlate, that.licensePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorName, resourceName, appointmentDate, licensePlate);
    }

    @Override
    public String toString() {
        return "AppointmentFormDataDto[" +
                "vendorName=" + vendorName + ", " +
                "resourceName=" + resourceName + ", " +
                "appointmentDate=" + appointmentDate + ", " +
                "licensePlate=" + licensePlate + ']';
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public ZonedDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setAppointmentDate(String appointmentDate) {
        try {
            this.appointmentDate = ZonedDateTime.parse(appointmentDate);
        } catch (DateTimeParseException e) {
            this.appointmentDate = ZonedDateTime.of(LocalDateTime.parse(appointmentDate), ZoneId.systemDefault());
        }
    }


    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
