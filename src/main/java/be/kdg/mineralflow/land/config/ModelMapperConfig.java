package be.kdg.mineralflow.land.config;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.Visit;
import be.kdg.mineralflow.land.business.domain.Weighbridge;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckDto;
import be.kdg.mineralflow.land.presentation.controller.dto.VisitDto;
import be.kdg.mineralflow.land.presentation.controller.dto.WeighbridgeDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(UnloadingRequest.class, TruckDto.class).setProvider(request -> {
            UnloadingRequest source = (UnloadingRequest) request.getSource();
            Visit visit = source.getVisit();
            Weighbridge weighbridge = visit.getWeighBridge();
            return new TruckDto(source.getLicensePlate(),
                    new VisitDto(visit.getArrivalTime(),
                            visit.getLeavingTime(),
                            new WeighbridgeDto(weighbridge.getWeighbridgeNumber())));});
        return modelMapper;
    }

}
