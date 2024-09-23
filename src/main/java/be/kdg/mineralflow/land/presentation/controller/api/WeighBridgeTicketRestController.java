package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.presentation.controller.dto.PlaceHolderDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/WeighBridgeTickets")
public class WeighBridgeTicketRestController {

    @PatchMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PlaceHolderDto weighBridgeTicketUpdate(@RequestBody @Valid PlaceHolderDto placeHolderDto) {
//        return new GrondstofDto(grondstofService.addGrondstof(grondstofje.toSource()));
        return new PlaceHolderDto();
    }

}
