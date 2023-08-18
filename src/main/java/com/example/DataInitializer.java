package com.example;

import com.example.court.Court;
import com.example.court.CourtService;
import com.example.courtType.CourtType;
import com.example.courtType.CourtTypeService;
import com.example.customer.Customer;
import com.example.customer.CustomerService;
import com.example.reservation.Reservation;
import com.example.reservation.ReservationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Třída sloužící ke configuraci aplikace
 */
@Configuration
public class DataInitializer {
    private final DataInitialization properties;
    private final CourtService courtService;
    private final CourtTypeService courtTypeService;
    private final CustomerService customerService;
    private final ReservationService reservationService;

    @Autowired
    public DataInitializer(DataInitialization properties, CourtService courtService, CourtTypeService courtTypeService,
                           CustomerService customerService, ReservationService reservationService) {
        this.properties = properties;
        this.courtService = courtService;
        this.courtTypeService = courtTypeService;
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    /**
     * Metoda inicializující data
     */
    @PostConstruct
    public void initializeData() {
        if (properties.isApp()) {
            CourtType clayCourtType = new CourtType();
            clayCourtType.setType("Antukový povrch");
            clayCourtType.setPrice(4);
            courtTypeService.addCourtType(clayCourtType);

            CourtType grassCourtType = new CourtType();
            grassCourtType.setType("Umělý trávník");
            grassCourtType.setPrice(3);
            courtTypeService.addCourtType(grassCourtType);

            Court clayCourt = new Court();
            clayCourt.setCourtTypeId(1);
            courtService.addCourt(clayCourt);

            Court clayCourt1 = new Court();
            clayCourt1.setCourtTypeId(1);
            courtService.addCourt(clayCourt1);

            Court grassCourt = new Court();
            grassCourt.setCourtTypeId(2);
            courtService.addCourt(grassCourt);

            Court grassCourt1 = new Court();
            grassCourt1.setCourtTypeId(2);
            courtService.addCourt(grassCourt1);
        } else if (properties.isTest()) {
            for (int i = 0; i < 4; i++) {
                courtService.addCourt(new Court());
                courtTypeService.addCourtType(new CourtType());
                customerService.addCustomer(new Customer());
                reservationService.addReservation(new Reservation());
            }
        }
    }
}
