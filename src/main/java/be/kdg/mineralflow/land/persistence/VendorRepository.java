package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingWithoutAppointment;
import be.kdg.mineralflow.land.business.domain.warehouse.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VendorRepository  extends JpaRepository<Vendor, UUID> {
    Vendor findByName(String name);
}
