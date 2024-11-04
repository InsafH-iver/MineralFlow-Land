package be.kdg.mineralflow.land.business.util.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TruckAppointmentArrivalResponse.class, name = "appointment"),
        @JsonSubTypes.Type(value = TruckArrivalResponse.class, name = "queue")
})
public class TruckArrivalResponse {

    boolean gateStatus;

    public TruckArrivalResponse(boolean gateStatus) {
        this.gateStatus = gateStatus;
    }
    @JsonProperty
    public boolean gateStatus() {
        return gateStatus;
    }
}
