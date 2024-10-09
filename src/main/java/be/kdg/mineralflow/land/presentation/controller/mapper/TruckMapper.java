package be.kdg.mineralflow.land.presentation.controller.mapper;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.Visit;
import be.kdg.mineralflow.land.business.domain.Weighbridge;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckDto;
import be.kdg.mineralflow.land.presentation.controller.dto.VisitDto;
import be.kdg.mineralflow.land.presentation.controller.dto.WeighbridgeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TruckMapper {
    static TruckMapper INSTANCE = Mappers.getMapper(TruckMapper.class);

    @Mapping(source = "visit", target = "visit")
    TruckDto mapUnloadingRequestToTruckDto(UnloadingRequest unloadingRequest);
    @Mapping(source = "weighBridge", target = "weighbridge")

    VisitDto mapVisitToVisitDto(Visit visit);
    WeighbridgeDto mapWeighBridgeToWeighBridgeDto(Weighbridge weighbridge);


}
