package com.example.customer;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    @Test
    @Order(2)
    void addCustomer() {
        customerService.addCustomer(new Customer());
        assertEquals(5, customerService.getAllCustomers().size());
    }

    @Test
    @Order(1)
    void getAllCustomers() {
        List<Customer> result = customerService.getAllCustomers();
        assertEquals(4, result.size());
    }

    @Test
    @Order(3)
    void deleteCustomer() {
        customerService.deleteCustomer(5);
        assertEquals(4, customerService.getAllCustomers().size());
    }

    @Test
    @Order(4)
    void updateCustomer() {
        customerService.updateCustomer(1, 123456789, "Karel Novotný");
        assertEquals(123456789, customerService.selectById(1).getPhone());
        assertEquals("Karel Novotný", customerService.selectById(1).getName());
    }

    @Test
    @Order(5)
    void selectById() {
        Customer customer = customerService.selectById(1);
        assertEquals(1, customer.getId());
    }

    @Test
    @Order(6)
    void validId() {
        assertTrue(customerService.validId(1));
        assertFalse(customerService.validId(999));
    }
}
