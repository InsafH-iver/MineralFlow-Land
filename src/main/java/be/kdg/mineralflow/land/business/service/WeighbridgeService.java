package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.Weighbridge;
import be.kdg.mineralflow.land.business.util.ExceptionHandlingHelper;
import be.kdg.mineralflow.land.persistence.WeighbridgeRepository;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class WeighbridgeService {
    public static final Logger logger = Logger
            .getLogger(WeighbridgeService.class.getName());

    private final WeighbridgeRepository weighbridgeRepository;

    public WeighbridgeService(WeighbridgeRepository weighbridgeRepository) {
        this.weighbridgeRepository = weighbridgeRepository;
    }

    public int getWeighBridgeNumber() {
        logger.info("weighbridge number is being fetched");
        Weighbridge weighbridge = getWeighbridge();
        int weighbridgeNumber = weighbridge.getWeighbridgeNumber();
        logger.info(String.format("Weighbridge number %d has been fetched", weighbridgeNumber));
        return weighbridgeNumber;
    }

    private Weighbridge getWeighbridge() {
        return weighbridgeRepository.findTopByOrderByWeighbridgeNumber()
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(
                        "No weighbridge found"
                ));
    }
}
