package com.example.customer;

import com.example.TennisClubApplication;
import com.example.court.Court;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servisní třída starající se o všechen přístup k databázi týkající se zákazníků
 */
@Service
public class CustomerService {
    /**
     * Metoda která přidá zákazníka do databáze
     *
     * @param customer zákazník kterého chceme přidat do databáze
     */
    public void addCustomer(Customer customer) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.persist(customer);

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která vrátí list všech nesmazaných zákazníků
     *
     * @return list nesmazaných zákazníků
     */
    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            allCustomers = session.createQuery("from Customer c where c.deleted = false", Customer.class)
                    .list();

            session.getTransaction().commit();
        }

        return allCustomers;
    }

    /**
     * Metoda která smaže zákazníka s daným id z databáze
     *
     * @param id id zákazníka kterého chceme smazat
     */
    public void deleteCustomer(Integer id) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.createQuery("select c from Customer c where c.id = :id", Customer.class)
                    .setParameter("id", id)
                    .uniqueResult()
                    .setDeleted(true);

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která upraví zákazníka s daným id
     *
     * @param id id zákazníka kterého chceme upravit
     * @param phone nové telefonní číslo zákazníka
     * @param name nové jméno zákazníka
     */
    public void updateCustomer(Integer id, Integer phone, String name) {
        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            Customer customer = session.createQuery("select c from Customer c where c.id = :id", Customer.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (phone != null) {
                customer.setPhone(phone);
            }

            if (name != null) {
                customer.setName(name);
            }

            session.getTransaction().commit();
        }
    }

    /**
     * Metoda která najde a vráti zákazníka se zadaným id
     *
     * @param id id zákazníka kterého chceme najít a vrátit
     * @return zákazník se zadaným id
     */
    public Customer selectById(Integer id) {
        Customer customer;

        try (Session session = TennisClubApplication.repository.getSessionFactory().openSession()) {
            session.beginTransaction();

            customer = session.createQuery("select c from Customer c where c.id = :id", Customer.class)
                    .setParameter("id", id)
                    .uniqueResult();

            session.getTransaction().commit();
        }

        return customer;
    }

    /**
     * Metoda která zkontroluje validitu id zákazníka
     *
     * @param id id zákazníka kterého chceme zkontrolovat
     * @return true pro validní id jinak false
     */
    public boolean validId(Integer id) {
        return selectById(id) != null;
    }
}
