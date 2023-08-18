package com.example.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Třída reprezentující kontroler endpointů týkající se rezervací
 */
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * POST endpoint na url adrese /api/reservation/add
     *
     * @param reservation rezervace kterou chceme přidat do databáze
     * @return chybový status pokud se pokusíme přidat rezervaci s nevalidními daty nebo ok status s cenou rezervace
     */
    @PostMapping("/add")
    public ResponseEntity<String> addReservation(@RequestBody Reservation reservation) {
        if (!reservationService.validReservation(reservation)) {
            return ResponseEntity.badRequest().body("Invalid reservation data");
        }

        reservationService.checkForNewOrReturningCustomer(reservation);
        reservationService.addReservation(reservation);

        return ResponseEntity.ok("Reservation price: " +
                reservationService.getReservationPrice(reservation) + "Kč");
    }

    /**
     * GET endpoint na url adrese /api/reservation
     *
     * @return list všech nesmazaných rezervací v databázi
     */
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    /**
     * DELETE endpoint na url adrese /api/reservation/delete/{id}
     *
     * @param id id rezervace kterou chceme smazat z databáze
     * @return chybový status pokud se pokusíme smazat rezervaci s neexistujícím id nebo ok status
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer id) {
        if (!reservationService.validId(id)) {
            return ResponseEntity.badRequest().body("Invalid id");
        }

        reservationService.deleteReservation(id);
        return ResponseEntity.ok("Successfully deleted");
    }

    /**
     * PUT endpoint na url adrese /api/reservation/update/{id}
     *
     * @param id id rezervace kterou chceme upravit v databázi
     * @param customerName nové jméno zákazníka
     * @param customerPhone nové telefonní číslo zákazníka
     * @param courtId nové id kurtu
     * @param gameType nový typ hry
     * @param reservationDate nové datum na který chceme rezervaci
     * @param fromTime nový čas od
     * @param toTime nový čas do
     * @return chybový status pokud se pokusíme upravit kurt s neexistujícím id nebo na neplatný čas, ok status jinak
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateReservation(@PathVariable Integer id,
                                   @RequestParam(required = false) String customerName,
                                   @RequestParam(required = false) Integer customerPhone,
                                   @RequestParam(required = false) Integer courtId,
                                   @RequestParam(required = false) GameType gameType,
                                   @RequestParam(required = false) String reservationDate,
                                   @RequestParam(required = false) String fromTime,
                                   @RequestParam(required = false) String toTime) {

        if (!reservationService.validId(id)) {
            return ResponseEntity.badRequest().body("Invalid id");
        }

        LocalDate date = null;
        LocalTime from = null;
        LocalTime to = null;

        if (reservationDate != null) {
            try {
                date = LocalDate.parse(reservationDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            } catch(Exception e) {
                return ResponseEntity.badRequest().body("Invalid reservation date");
            }
        }

        if (fromTime != null) {
            try {
                from = LocalTime.parse(fromTime);
            } catch(Exception e) {
                return ResponseEntity.badRequest().body("Invalid reservation time");
            }
        }

        if (toTime != null) {
            try {
                to = LocalTime.parse(toTime);
            } catch(Exception e) {
                return ResponseEntity.badRequest().body("Invalid reservation time");
            }
        }

        reservationService.updateReservation(id, customerName, customerPhone, courtId, gameType, date, from, to);
        return ResponseEntity.ok("Successfully updated");
    }

    /**
     * GET endpoint na url adrese /api/reservation/{courtId}
     *
     * @param courtId Id kurtu kterého chceme rezervace
     * @return list rezervací na kurt s daným id
     */
    @GetMapping("/{courtId}")
    public List<Reservation> getAllReservationsByCourtId(@PathVariable Integer courtId) {
        return reservationService.getAllReservationsByCourtId(courtId);
    }

    /**
     * GET endpoint na url adrese /api/reservation/phone/{customerPhone}
     *
     * @param customerPhone telefonní číslo zákazníka na kterého chceme rezervace
     * @param future boolean pokud chceme rezervace jen v budoucnosti
     * @return list rezervací na dané telefonní číslo zákazníka
     */
    @GetMapping("/phone/{customerPhone}")
    public List<Reservation> getAllReservationsByCustomerPhone(@PathVariable Integer customerPhone,
                                                               @RequestParam(required = false) boolean future) {
        return reservationService.getAllReservationsByCustomerPhone(customerPhone, future);
    }
}
