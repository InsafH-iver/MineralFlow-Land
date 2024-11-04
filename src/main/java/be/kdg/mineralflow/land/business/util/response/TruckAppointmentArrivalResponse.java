package be.kdg.mineralflow.land.business.util.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Objects;

public class TruckAppointmentArrivalResponse extends TruckArrivalResponse {
    private final ZonedDateTime returnTimeOfTruck;

    public TruckAppointmentArrivalResponse(boolean gateStatus, ZonedDateTime returnTimeOfTruck) {
        super(gateStatus);
        this.returnTimeOfTruck = returnTimeOfTruck;
    }

    @JsonProperty
    public ZonedDateTime returnTimeOfTruck() {
        return returnTimeOfTruck;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TruckAppointmentArrivalResponse) obj;
        return Objects.equals(this.returnTimeOfTruck, that.returnTimeOfTruck);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnTimeOfTruck);
    }

    @Override
    public String toString() {
        return "TruckAppointmentArrivalResponse[" +
                "returnTimeOfTruck=" + returnTimeOfTruck + ']';
    }

}
