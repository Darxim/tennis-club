package com.example.court;

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
class CourtServiceTest {
    @Autowired
    private CourtService courtService;

    @Test
    @Order(2)
    void addCourt() {
        courtService.addCourt(new Court());
        assertEquals(5, courtService.getAllCourts().size());
    }

    @Test
    @Order(1)
    void getAllCourts() {
        List<Court> result = courtService.getAllCourts();
        assertEquals(4, result.size());
    }

    @Test
    @Order(3)
    void deleteCourt() {
        courtService.deleteCourt(5);
        assertEquals(4, courtService.getAllCourts().size());
    }

    @Test
    @Order(4)
    void updateCourt() {
        courtService.updateCourt(1, 42);
        assertEquals(42, courtService.selectById(1).getCourtTypeId());
    }

    @Test
    @Order(5)
    void selectById() {
        Court court = courtService.selectById(1);
        assertEquals(1, court.getId());
    }

    @Test
    @Order(6)
    void validId() {
        assertTrue(courtService.validId(1));
        assertFalse(courtService.validId(999));
    }
}
