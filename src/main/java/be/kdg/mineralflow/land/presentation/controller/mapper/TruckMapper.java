package be.kdg.mineralflow.land.presentation.controller.mapper;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.Visit;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckDto;
import be.kdg.mineralflow.land.presentation.controller.dto.VisitDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TruckMapper {
    TruckMapper INSTANCE = Mappers.getMapper(TruckMapper.class);

    @Mapping(source = "visit", target = "visit")
    TruckDto mapUnloadingRequestToTruckDto(UnloadingRequest unloadingRequest);
    VisitDto mapVisitToVisitDto(Visit visit);


}
