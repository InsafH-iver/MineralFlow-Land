package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.Weighbridge;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.persistence.WeighbridgeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class WeighbridgeManager {
    public static final Logger logger = Logger
            .getLogger(WeighbridgeManager.class.getName());

    private final WeighbridgeRepository weighbridgeRepository;

    public WeighbridgeManager(WeighbridgeRepository weighbridgeRepository) {
        this.weighbridgeRepository = weighbridgeRepository;
    }

    public Weighbridge getWeighBridgeNumber() {
        Optional<Weighbridge> weighbridge = weighbridgeRepository.findRandomWeighbridge();
        if (weighbridge.isEmpty()) {
            throw new NoItemFoundException("No weighbridge found");
        }
        return weighbridge.get();
    }
}
