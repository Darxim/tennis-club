package com.example.reservation;

import com.example.court.CourtService;
import com.example.courtType.CourtTypeService;
import com.example.customer.CustomerService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationServiceTest {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    private CourtService courtService;

    @Autowired
    private CourtTypeService courtTypeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationService reservationService;

    @Test
    @Order(2)
    void addReservation() {
        reservationService.addReservation(new Reservation());
        assertEquals(5, reservationService.getAllReservations().size());
    }

    @Test
    @Order(4)
    void checkForNewOrReturningCustomer() {
        Reservation reservation = new Reservation();
        reservation.setCustomerPhone(123123123);
        reservation.setCustomerName("Pavel Svoboda");

        assertEquals(4, customerService.getAllCustomers().size());
        reservationService.checkForNewOrReturningCustomer(reservation);
        assertEquals(5, customerService.getAllCustomers().size());
        reservationService.checkForNewOrReturningCustomer(reservation);
        assertEquals(5, customerService.getAllCustomers().size());
    }

    @Test
    @Order(5)
    void validReservation() {
        Reservation reservation = new Reservation();
        reservation.setCourtId(3);
        reservation.setReservationDate(LocalDate.parse("22-08-2023", formatter));
        reservation.setFromTime(LocalTime.parse("10:00"));
        reservation.setToTime(LocalTime.parse("09:00"));
        assertFalse(reservationService.validReservation(reservation));

        reservation.setToTime(LocalTime.parse("11:00"));
        assertTrue(reservationService.validReservation(reservation));
        reservationService.addReservation(reservation);

        Reservation reservation1 = new Reservation();
        reservation1.setCourtId(3);
        reservation1.setReservationDate(LocalDate.parse("22-08-2023", formatter));
        reservation1.setFromTime(LocalTime.parse("11:00"));
        reservation1.setToTime(LocalTime.parse("12:00"));
        assertTrue(reservationService.validReservation(reservation1));
        reservationService.addReservation(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setCourtId(3);
        reservation2.setReservationDate(LocalDate.parse("22-08-2023", formatter));
        reservation2.setFromTime(LocalTime.parse("10:30"));
        reservation2.setToTime(LocalTime.parse("12:00"));
        assertFalse(reservationService.validReservation(reservation2));
    }

    @Test
    @Order(6)
    void getReservationPrice() {
        courtTypeService.updateCourtType(1, "", 4);
        courtService.updateCourt(1, 1);
        courtTypeService.updateCourtType(2, "", 3);
        courtService.updateCourt(2, 2);

        Reservation reservation = new Reservation();
        reservation.setCourtId(1);
        reservation.setGameType(GameType.TWO_PLAYERS);
        reservation.setReservationDate(LocalDate.parse("23-08-2023", formatter));
        reservation.setFromTime(LocalTime.parse("15:23"));
        reservation.setToTime(LocalTime.parse("17:48"));

        assertEquals(580, reservationService.getReservationPrice(reservation));
        reservation.setGameType(GameType.FOUR_PLAYERS);
        assertEquals(870, reservationService.getReservationPrice(reservation));
        reservation.setCourtId(2);
        assertEquals(652.5, reservationService.getReservationPrice(reservation));
        reservation.setGameType(GameType.TWO_PLAYERS);
        assertEquals(435, reservationService.getReservationPrice(reservation));
    }

    @Test
    @Order(1)
    void getAllReservations() {
        List<Reservation> result = reservationService.getAllReservations();
        assertEquals(4, result.size());
    }

    @Test
    @Order(3)
    void deleteReservation() {
        reservationService.deleteReservation(5);
        assertEquals(4, reservationService.getAllReservations().size());
    }

    @Test
    @Order(7)
    void updateReservation() {
        reservationService.updateReservation(1, "Karel Novák", 666666666, 1,
                GameType.FOUR_PLAYERS, LocalDate.parse("25-08-2023", formatter),
                LocalTime.parse("10:00"), LocalTime.parse("12:00"));
        assertEquals("Karel Novák", reservationService.selectById(1).getCustomerName());
        assertEquals(666666666, reservationService.selectById(1).getCustomerPhone());
        assertEquals(1, reservationService.selectById(1).getCourtId());
        assertEquals(GameType.FOUR_PLAYERS, reservationService.selectById(1).getGameType());
        assertEquals(LocalDate.parse("25-08-2023", formatter), reservationService.selectById(1).getReservationDate());
        assertEquals(LocalTime.parse("10:00"), reservationService.selectById(1).getFromTime());
        assertEquals(LocalTime.parse("12:00"), reservationService.selectById(1).getToTime());
    }

    @Test
    @Order(8)
    void getAllReservationsByCourtId() {
        reservationService.updateReservation(2, null, null, 2, null,
                null, null, null);
        reservationService.updateReservation(3, null, null, 1, null,
                null, null, null);
        reservationService.updateReservation(4, null, null, 1, null,
                null, null, null);

        assertEquals(3, reservationService.getAllReservationsByCourtId(1).size());
        assertEquals(1, reservationService.getAllReservationsByCourtId(2).size());
        assertEquals(2, reservationService.getAllReservationsByCourtId(3).size());
    }

    @Test
    @Order(9)
    void getAllReservationsByCustomerPhone() {
        reservationService.updateReservation(2, null, 666666666, null, null,
                null, null, null);
        reservationService.updateReservation(3, null, 666666667, null, null,
                null, null, null);
        reservationService.updateReservation(4, null, 666666666, null, null,
                null, null, null);

        assertEquals(3, reservationService.getAllReservationsByCustomerPhone(
                666666666, false).size());
        assertEquals(1, reservationService.getAllReservationsByCustomerPhone(
                666666667, false).size());
    }

    @Test
    @Order(10)
    void selectById() {
        Reservation reservation = reservationService.selectById(1);
        assertEquals(1, reservation.getId());
    }

    @Test
    @Order(11)
    void validId() {
        assertTrue(reservationService.validId(1));
        assertFalse(reservationService.validId(999));
    }
}
