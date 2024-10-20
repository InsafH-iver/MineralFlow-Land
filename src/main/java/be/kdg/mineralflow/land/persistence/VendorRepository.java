package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.warehouse.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VendorRepository  extends JpaRepository<Vendor, UUID> {
    Optional<Vendor> findByName(String name);
}
