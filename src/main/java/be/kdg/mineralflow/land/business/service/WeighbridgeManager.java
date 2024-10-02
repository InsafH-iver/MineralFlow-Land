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
        logger.info("weighbridge number is being fetched");
        Optional<Weighbridge> optionalWeighbridge = weighbridgeRepository.findTopByOrderByWeighbridgeNumber();
        if (optionalWeighbridge.isEmpty()) {
            String text = "No weighbridge found";
            logger.severe(text);
            throw new NoItemFoundException(text);
        }
        Weighbridge weighbridge = optionalWeighbridge.get();
        logger.info(String.format("Weighbridge number %d has been fetched", weighbridge.getWeighbridgeNumber()));
        return weighbridge;
    }
}
