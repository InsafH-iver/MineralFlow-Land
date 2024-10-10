package be.kdg.mineralflow.land.presentation.controller.mapper;

import be.kdg.mineralflow.land.business.domain.TimeSlot;
import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.UnloadingWithoutAppointment;
import be.kdg.mineralflow.land.presentation.controller.dto.AppointmentDto;
import be.kdg.mineralflow.land.presentation.controller.dto.PlanningDto;
import be.kdg.mineralflow.land.presentation.controller.dto.TimeslotDto;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentMapper {
    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);
    @Mapping(source = "timeSlot.startOfTimeSlot", target = "timeslotDto.startOfTimeslot")
    @Mapping(source = "timeSlot.endOfTimeSlot", target = "timeslotDto.endOfTimeslot")
    @Mapping(source = "licensePlate",target = "truckDto.licensePlate")
    @Mapping(source = "visit", target = "truckDto.visit")
    AppointmentDto mapUnloadingAppointmentToAppointmentDto(UnloadingAppointment unloadingAppointment);

    @Mapping(source = "licensePlate", target = "licensePlate")
    @Mapping(source = "visit", target = "visit")
    TruckDto mapUnloadingWithoutAppointmentToTruckDto(UnloadingWithoutAppointment unloadingWithoutAppointment);

    // Map lists to lists
    List<AppointmentDto> mapAppointments(List<UnloadingAppointment> unloadingAppointments);
    List<TruckDto> mapQueue(List<UnloadingWithoutAppointment> queue);
}
