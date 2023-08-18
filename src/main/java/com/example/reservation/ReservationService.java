package com.example.reservation;

import com.example.TennisClubApplication;
import com.example.court.Court;
import com.example.courtType.CourtType;
import com.example.customer.Customer;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servisní třída starající se o všechen přístup k databázi týkající se rezervací
 */
@Service
public class ReservationService {
    /**
     * Metoda která přidá rezervaci do databáze
     *
     * @param reservation rezervace kterou chceme přidat do databáze
     */
    public void addReservation(Reservation reservation) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.persist(reservation);

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která zkontroluje jestli rezervace není na telefoní číslo zákazníka, které ještě nemáme
     * v databázi a popřípadě ho do databáze přidá/obnoví
     *
     * @param reservation rezervace kterou chceme zkontrolovat
     */
    public void checkForNewOrReturningCustomer(Reservation reservation) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            Customer wantedCustomer = session.createQuery("from Customer c where c.phone = :phone", Customer.class)
                    .setParameter("phone", reservation.getCustomerPhone())
                    .uniqueResult();

            if (wantedCustomer == null) {
                Customer newCustomer = new Customer();
                newCustomer.setPhone(reservation.getCustomerPhone());
                newCustomer.setName(reservation.getCustomerName());
                session.persist(newCustomer);
            } else if (wantedCustomer.getDeleted()) {
                wantedCustomer.setDeleted(false);
                wantedCustomer.setName(reservation.getCustomerName());
            }

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která zkontroluje validitu rezervace. Jestli je rezervace na smysluplný časový interval
     * a jestli se nepřekrývá s jinou rezervací na daném kurtu
     *
     * @param reservation rezervace kterou chceme zkontrolovat
     * @return true pro validní rezervaci jinak false
     */
    public boolean validReservation(Reservation reservation) {
        Long overlapCount;

        if (reservation.getFromTime().isAfter(reservation.getToTime())) {
            return false;
        }

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            overlapCount = session.createQuery("select count(r) from Reservation r where r.courtId = :courtId " +
                            "and r.reservationDate = :reservationDate " +
                            "and ((r.fromTime <= :fromTime and r.toTime > :fromTime) " +
                            "or (r.fromTime < :toTime and r.toTime >= :toTime))", Long.class)
                            .setParameter("courtId", reservation.getCourtId())
                            .setParameter("reservationDate", reservation.getReservationDate())
                            .setParameter("fromTime", reservation.getFromTime())
                            .setParameter("toTime", reservation.getToTime())
                            .getSingleResult();

            session.getTransaction().commit();
        }

        return overlapCount <= 0;
    }

    /**
     * Metoda která vypočítá a vráti cenu rezervace podle typu kurtu a typu hry
     *
     * @param reservation rezervace pro kterou chceme vypočítat cenu
     * @return cena rezervace kurtu
     */
    public double getReservationPrice(Reservation reservation) {
        double price;
        Court court;
        CourtType courtType;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            court = session.createQuery("select c from Court c where c.id = :courtId", Court.class)
                    .setParameter("courtId", reservation.getCourtId())
                    .uniqueResult();

            courtType = session.createQuery("select ct from CourtType ct where ct.id = :courtTypeId", CourtType.class)
                    .setParameter("courtTypeId", court.getCourtTypeId())
                    .uniqueResult();

            session.getTransaction().commit();
        }

        int fromMinutes = reservation.getFromTime().getHour() * 60 + reservation.getFromTime().getMinute();
        int toMinutes = reservation.getToTime().getHour() * 60 + reservation.getToTime().getMinute();
        int reservationDuration = toMinutes - fromMinutes;

        price = reservationDuration * courtType.getPrice();
        if (reservation.getGameType() == GameType.FOUR_PLAYERS) {
            price *= 1.5;
        }

        return price;
    }

    /**
     * Metoda která vrátí list všech nesmazaných rezervací
     *
     * @return list nesmazaných rezervací
     */
    public List<Reservation> getAllReservations() {
        List<Reservation> allReservations;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            allReservations = session.createQuery("from Reservation r where r.deleted = false", Reservation.class)
                    .list();

            session.getTransaction().commit();
        }

        return allReservations;
    }

    /**
     * Metoda která smaže rezervaci s daným id z databáze
     *
     * @param id id rezervace kterou chceme smazat
     */
    public void deleteReservation(Integer id) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createQuery("select r from Reservation r where r.id = :id", Reservation.class)
                    .setParameter("id", id)
                    .uniqueResult()
                    .setDeleted(true);

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která upraví rezervaci s daným id
     *
     * @param id id rezervace kterou chceme upravit
     * @param customerName nové jméno zákazníka
     * @param customerPhone nové telefonní číslo zákazníka
     * @param courtId nové id kurtu
     * @param gameType nový typ hry
     * @param reservationDate nové datum na který chceme rezervaci
     * @param fromTime nový čas od
     * @param toTime nový čas do
     */
    public void updateReservation(Integer id, String customerName, Integer customerPhone, Integer courtId,
                                  GameType gameType, LocalDate reservationDate, LocalTime fromTime, LocalTime toTime) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            Reservation reservation =
                    session.createQuery("select r from Reservation r where r.id = :id", Reservation.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (customerName != null) {
                reservation.setCustomerName(customerName);
            }

            if (customerPhone != null) {
                reservation.setCustomerPhone(customerPhone);
            }

            if (courtId != null) {
                reservation.setCourtId(courtId);
            }

            if (gameType != null) {
                reservation.setGameType(gameType);
            }

            if (reservationDate != null) {
                reservation.setReservationDate(reservationDate);
            }

            if (fromTime != null) {
                reservation.setFromTime(fromTime);
            }

            if (toTime != null) {
                reservation.setToTime(toTime);
            }

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která vráti všechny rezervace na dané id kurtu
     *
     * @param courtId Id kurtu kterého chceme rezervace
     * @return list rezervací na kurt s daným id
     */
    public List<Reservation> getAllReservationsByCourtId(Integer courtId) {
        List<Reservation> allReservationsByCourtId;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            allReservationsByCourtId =
                    session.createQuery("from Reservation r where r.courtId = :courtId " +
                                    "and r.deleted = false", Reservation.class)
                    .setParameter("courtId", courtId)
                    .list();

            session.getTransaction().commit();
        }

        return allReservationsByCourtId;
    }

    /**
     * Metoda která vrátí všechny rezervace na dané telefonní číslo s možností
     * vrátit jen rezervace v budouconosti
     *
     * @param customerPhone telefonní číslo zákazníka na kterého chceme rezervace
     * @param future boolean pokud chceme rezervace jen v budoucnosti
     * @return list rezervací na dané telefonní číslo zákazníka
     */
    public List<Reservation> getAllReservationsByCustomerPhone(Integer customerPhone, boolean future) {
        List<Reservation> allReservationsByCustomerPhone;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            allReservationsByCustomerPhone =
                    session.createQuery("from Reservation r where r.customerPhone = :customerPhone " +
                                    "and r.deleted = false", Reservation.class)
                            .setParameter("customerPhone", customerPhone)
                            .list();

            session.getTransaction().commit();
        }

        if (future) {
            return allReservationsByCustomerPhone
                    .stream()
                    .filter(reservation -> reservation.getReservationDate().isAfter(LocalDate.now()))
                    .collect(Collectors.toList());
        }

        return allReservationsByCustomerPhone;
    }

    /**
     * Metoda která najde a vráti rezervaci se zadaným id
     *
     * @param id id rezervace kterou chceme najít a vrátit
     * @return rezervace se zadaným id
     */
    public Reservation selectById(Integer id) {
        Reservation reservation;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            reservation = session.createQuery("select r from Reservation r where r.id = :id", Reservation.class)
                    .setParameter("id", id)
                    .uniqueResult();

            session.getTransaction().commit();
        }

        return reservation;
    }

    /**
     * Metoda která zkontroluje validitu id rezervace
     *
     * @param id id rezervace kterou chceme zkontrolovat
     * @return true pro validní id jinak false
     */
    public boolean validId(Integer id) {
        return selectById(id) != null;
    }
}
