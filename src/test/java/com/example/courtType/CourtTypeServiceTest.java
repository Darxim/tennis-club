package com.example.courtType;

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
class CourtTypeServiceTest {
    @Autowired
    private CourtTypeService courtTypeService;

    @Test
    @Order(2)
    void addCourtType() {
        courtTypeService.addCourtType(new CourtType());
        assertEquals(5, courtTypeService.getAllCourtTypes().size());
    }

    @Test
    @Order(1)
    void getAllCourtTypes() {
        List<CourtType> result = courtTypeService.getAllCourtTypes();
        assertEquals(4, result.size());
    }

    @Test
    @Order(3)
    void deleteCourtType() {
        courtTypeService.deleteCourtType(5);
        assertEquals(4, courtTypeService.getAllCourtTypes().size());
    }

    @Test
    @Order(4)
    void updateCourtType() {
        courtTypeService.updateCourtType(1, "Antukový povrch", 4);
        assertEquals("Antukový povrch", courtTypeService.selectById(1).getType());
        assertEquals(4, courtTypeService.selectById(1).getPrice());
    }

    @Test
    @Order(5)
    void selectById() {
        CourtType courtType = courtTypeService.selectById(1);
        assertEquals(1, courtType.getId());
    }

    @Test
    @Order(6)
    void validId() {
        assertTrue(courtTypeService.validId(1));
        assertFalse(courtTypeService.validId(999));
    }
}
