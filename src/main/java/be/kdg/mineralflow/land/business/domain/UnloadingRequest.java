package be.kdg.mineralflow.land.business.domain;

import be.kdg.mineralflow.land.business.domain.warehouse.Resource;
import be.kdg.mineralflow.land.business.domain.warehouse.Vendor;
import be.kdg.mineralflow.land.business.util.WeighBridgeTicketResponse;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Logger;
import java.time.format.DateTimeFormatter;


@Entity
public class UnloadingRequest {
    public static final Logger logger = Logger
            .getLogger(UnloadingRequest.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String licensePlate;
    @OneToOne(cascade = CascadeType.ALL)
    private Visit visit;
    @ManyToOne
    private Resource resource;
    @ManyToOne
    private Vendor vendor;

    public UnloadingRequest(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    protected UnloadingRequest() {
    }

    public void setVisit(Visit visit) {
        logger.info(
                String.format("A truck has arrived at %s for this appointment",
                        visit.getArrivalTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))

        );
        this.visit = visit;
    }

    public void addWeightBridgeTicketToVisit(double startWeightAmountInTon,
                                             ZonedDateTime startWeightTimestamp) {
        this.visit.setWeighbridgeTicket(startWeightAmountInTon,
                startWeightTimestamp);
    }
    public double getNetWeightOfWeighBridgeTicket() {
        return visit.getNetWeightOfWeighBridgeTicket();
    }
    public void updateWeightBridgeTicketAtDeparture(double endWeightAmountInTon,
                                             ZonedDateTime endWeightTimestamp) {
        this.visit.updateEndWeightOfWeighbridgeTicket(endWeightAmountInTon, endWeightTimestamp);
    }

    public WeighBridgeTicketResponse getWeighBridgeTicketData(String licensePlate) {
        return visit.getWeighBridgeTicketData(licensePlate);
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getLicensePlate() {
        logger.info(String.format("Fetching license plate %s from unloading request", licensePlate));
        return licensePlate;
    }

    public boolean hasBeenOnWeighBridge(){
        return visit.hasWeighbridgeTicket();
    }

    public Visit getVisit() {
        return visit;
    }

    public UUID getResourceId() {
        return resource.getId();
    }

    public UUID getVendorId() {
        return vendor.getId();
    }

    @Override
    public String toString() {
        return "UnloadingRequest{" +
                "id=" + id +
                ", licensePlate='" + licensePlate + '\'' +
                ", visit=" + visit +
                '}';
    }


}
