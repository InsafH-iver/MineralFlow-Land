package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.util.WeighBridgeTicketResponse;
import org.springframework.stereotype.Service;

@Service
public class WeighBridgeTicketGeneratingService {

    private final PdfGeneratingService pdfGeneratingService;

    public WeighBridgeTicketGeneratingService(PdfGeneratingService pdfGeneratingService) {
        this.pdfGeneratingService = pdfGeneratingService;
    }

    public void generateWeighBridgeTicketPdf(WeighBridgeTicketResponse weighBridgeTicketResponse) {
        String fileName = String.format("WBT-%s.pdf", weighBridgeTicketResponse.hashCode());
        pdfGeneratingService.generatePdf(weighBridgeTicketResponse, "weighBridgeTicket", "weighbridge_ticket", fileName);
    }
}
