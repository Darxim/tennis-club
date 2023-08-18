package com.example.court;

import com.example.TennisClubApplication;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servisní třída starající se o všechen přístup k databázi týkající se kurtů
 */
@Service
public class CourtService {
    /**
     * Metoda která přidá kurt do databáze
     *
     * @param court kurt který chceme přidat do databáze
     */
    public void addCourt(Court court) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.persist(court);

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která vrátí list všech nesmazaných kurtů
     *
     * @return list nesmazaných kurtů
     */
    public List<Court> getAllCourts() {
        List<Court> allCourts;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            allCourts = session.createQuery("from Court c where c.deleted = false", Court.class)
                    .list();

            session.getTransaction().commit();
        }

        return allCourts;
    }

    /**
     * Metoda která smaže kurt s daným id z databáze
     *
     * @param id id kurtu který chceme smazat
     */
    public void deleteCourt(Integer id) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createQuery("select c from Court c where c.id = :id", Court.class)
                    .setParameter("id", id)
                    .uniqueResult()
                    .setDeleted(true);

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která upraví kurt s daným id
     *
     * @param id id kurtu který chceme upravit
     * @param courtTypeId nové id typu kurtu
     */
    public void updateCourt(Integer id, Integer courtTypeId) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            Court court = session.createQuery("select c from Court c where c.id = :id", Court.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (courtTypeId != null) {
                court.setCourtTypeId(courtTypeId);
            }

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která najde a vráti kurt se zadaným id
     *
     * @param id id kurtu který chceme najít a vrátit
     * @return kurt se zadaným id
     */
    public Court selectById(Integer id) {
        Court court;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            court = session.createQuery("select c from Court c where c.id = :id", Court.class)
                    .setParameter("id", id)
                    .uniqueResult();

            session.getTransaction().commit();
        }

        return court;
    }

    /**
     * Metoda která zkontroluje validitu id kurtu
     *
     * @param id id kurtu který chceme zkontrolovat
     * @return true pro validní id jinak false
     */
    public boolean validId(Integer id) {
        return selectById(id) != null;
    }
}
