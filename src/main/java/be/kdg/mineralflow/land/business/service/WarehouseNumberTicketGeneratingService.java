package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.util.WarehouseNumberResponse;
import org.springframework.stereotype.Service;

@Service
public class WarehouseNumberTicketGeneratingService {

    private final PdfGeneratingService pdfGeneratingService;

    public WarehouseNumberTicketGeneratingService(PdfGeneratingService pdfGeneratingService) {
        this.pdfGeneratingService = pdfGeneratingService;
    }

    public void generateWeighBridgeTicketPdf(WarehouseNumberResponse warehouseNumberResponse) {
        String fileName = String.format("Warehouse-number-%s.pdf", warehouseNumberResponse.hashCode());
        pdfGeneratingService.generatePdf(warehouseNumberResponse, "warehouseNumberTicket", "warehouse_number_ticket", fileName);
    }
}
