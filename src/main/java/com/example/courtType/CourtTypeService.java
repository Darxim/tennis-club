package com.example.courtType;

import com.example.TennisClubApplication;
import com.example.court.Court;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servisní třída starající se o všechen přístup k databázi týkající se typů kurtů
 */
@Service
public class CourtTypeService {
    /**
     * Metoda která přidá typ kurtu do databáze
     *
     * @param courtType typ kurtu který chceme přidat do databáze
     */
    public void addCourtType(CourtType courtType) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.persist(courtType);

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která vrátí list všech nesmazaných typů kurtů
     *
     * @return list nesmazaných typů kurtů
     */
    public List<CourtType> getAllCourtTypes() {
        List<CourtType> AllCourtTyp;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            AllCourtTyp = session.createQuery("from CourtType ct where ct.deleted = false", CourtType.class)
                    .list();

            session.getTransaction().commit();
        }

        return AllCourtTyp;
    }

    /**
     * Metoda která smaže typ kurtu s daným id z databáze
     *
     * @param id id typu kurtu který chceme smazat
     */
    public void deleteCourtType(Integer id) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createQuery("select ct from CourtType ct where ct.id = :id", CourtType.class)
                    .setParameter("id", id)
                    .uniqueResult()
                    .setDeleted(true);

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která upraví typ kurtu s daným id
     *
     * @param id id typu kurtu který chceme upravit
     * @param type nový typ kurtu
     * @param price nová cena daného typu kurtu
     */
    public void updateCourtType(Integer id, String type, Integer price) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            CourtType courtType =
                    session.createQuery("select ct from CourtType ct where ct.id = :id", CourtType.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (type != null) {
                courtType.setType(type);
            }

            if (price != null) {
                courtType.setPrice(price);
            }

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která najde a vráti typ kurtu se zadaným id
     *
     * @param id id typu kurtu který chceme najít a vrátit
     * @return typ kurtu se zadaným id
     */
    public CourtType selectById(Integer id) {
        CourtType courtType;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            courtType = session.createQuery("select ct from CourtType ct where ct.id = :id", CourtType.class)
                    .setParameter("id", id)
                    .uniqueResult();

            session.getTransaction().commit();
        }

        return courtType;
    }

    /**
     * Metoda která zkontroluje validitu id typu kurtu
     *
     * @param id id typu kurtu který chceme zkontrolovat
     * @return true pro validní id jinak false
     */
    public boolean validId(Integer id) {
        return selectById(id) != null;
    }
}
