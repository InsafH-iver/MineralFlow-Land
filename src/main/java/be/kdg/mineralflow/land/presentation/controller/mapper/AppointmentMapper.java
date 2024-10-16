package be.kdg.mineralflow.land.presentation.controller.mapper;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.domain.UnloadingWithoutAppointment;
import be.kdg.mineralflow.land.business.domain.Visit;
import be.kdg.mineralflow.land.presentation.controller.dto.AppointmentDto;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckDto;
import be.kdg.mineralflow.land.presentation.controller.dto.VisitDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);
    @Mapping(source = "timeSlot.startOfTimeSlot", target = "timeslot.startOfTimeslot")
    @Mapping(source = "timeSlot.endOfTimeSlot", target = "timeslot.endOfTimeslot")
    @Mapping(source = "licensePlate",target = "truck.licensePlate")
    @Mapping(source = "visit",target = "truck.visit")
    AppointmentDto mapUnloadingAppointmentToAppointmentDto(UnloadingAppointment unloadingAppointment);
    @Mapping(source = "visit", target = "visit")
    TruckDto mapUnloadingWithoutAppointmentToTruckDto(UnloadingWithoutAppointment unloadingWithoutAppointment);
    VisitDto mapVisitToVisitDto(Visit visit);
    List<AppointmentDto> mapAppointments(List<UnloadingAppointment> unloadingAppointments);

}
