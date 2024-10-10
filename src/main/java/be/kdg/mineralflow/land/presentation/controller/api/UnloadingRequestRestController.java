package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.domain.UnloadingWithoutAppointment;
import be.kdg.mineralflow.land.business.service.UnloadingRequestManager;
import be.kdg.mineralflow.land.business.util.TruckArrivalResponse;
import be.kdg.mineralflow.land.presentation.controller.dto.PlanningDto;
import be.kdg.mineralflow.land.presentation.controller.mapper.AppointmentMapper;
import be.kdg.mineralflow.land.presentation.controller.mapper.TruckMapper;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/UnloadingRequests")
public class UnloadingRequestRestController {
    public static final Logger logger = Logger
            .getLogger(UnloadingRequestRestController.class.getName());

    private final UnloadingRequestManager unloadingRequestManager;

    public UnloadingRequestRestController(UnloadingRequestManager unloadingRequestManager) {
        this.unloadingRequestManager = unloadingRequestManager;
    }

    @PostMapping("/visit/{licensePlate}")
    public ResponseEntity<TruckArrivalResponse> processTruckArrivalAtGate(@PathVariable String licensePlate) {
        ZonedDateTime timeOfArrival = ZonedDateTime.now(ZoneOffset.UTC);

        logger.info(String.format("A call has been made to process the truck with license plate %s and arrival time %s",
                licensePlate,
                timeOfArrival
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
        );
        TruckArrivalResponse arrivalResponse = unloadingRequestManager
                .processTruckArrivalAtGate(licensePlate, timeOfArrival);


        if (arrivalResponse.gateStatus()) {
            logger.info(String.format("The truck with license plate %s and arrival time %s has been granted entrance and has a visit",
                    licensePlate,
                    timeOfArrival
                            .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(arrivalResponse);
        } else {// kan veranderen vanaf dat het fifo gedeelte erbij komt
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(arrivalResponse);
        }
    }
    public ResponseEntity<PlanningDto> getPlanning(){
        List<UnloadingAppointment> unloadingAppointments = unloadingRequestManager.getAllUnloadingAppointments();
        List<UnloadingWithoutAppointment> queue = unloadingRequestManager.getAllUnloadingWithoutAppointments();
        logger.info(String.format("getPlanning found: queue(%d), with first: %s",queue.size(),queue.getFirst()));
        logger.info(String.format("getPlanning found: appointment(%d), with first: %s",unloadingAppointments.size(),unloadingAppointments.getFirst()));
        ResponseEntity<PlanningDto> response = ResponseEntity.status(HttpStatus.OK).body(
                new PlanningDto(AppointmentMapper.INSTANCE.mapQueue(queue),AppointmentMapper.INSTANCE.mapAppointments(unloadingAppointments))
        );
        logger.info(String.format("getPlanning sending back... : %s",response.getBody()));
        return response;
    }
}
