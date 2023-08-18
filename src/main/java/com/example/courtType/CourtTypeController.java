package com.example.courtType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Třída reprezentující kontroler endpointů týkající se typů kurtů
 */
@RestController
@RequestMapping("/api/court_type")
public class CourtTypeController {
    private final CourtTypeService courtTypeService;

    @Autowired
    public CourtTypeController(CourtTypeService courtTypeService) {
        this.courtTypeService = courtTypeService;
    }

    /**
     * POST endpoint na url adrese /api/court_type/add
     *
     * @param courtType typ kurtu který chceme přidat do databáze
     */
    @PostMapping("/add")
    public void addCourtType(@RequestBody CourtType courtType) {
        courtTypeService.addCourtType(courtType);
    }

    /**
     * GET endpoint na url adrese /api/court_type
     *
     * @return list všech nesmazaných typů kurtů v databázi
     */
    @GetMapping
    public List<CourtType> getAllCourtTypes() {
        return courtTypeService.getAllCourtTypes();
    }

    /**
     * DELETE endpoint na url adrese /api/court_type/delete/{id}
     *
     * @param id id typu kurtu který chceme smazat z databáze
     * @return chybový status pokud se pokusíme smazat typ kurtu s neexistujícím id nebo ok status
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourtType(@PathVariable Integer id) {
        if (!courtTypeService.validId(id)) {
            return ResponseEntity.badRequest().body("Invalid id");
        }

        courtTypeService.deleteCourtType(id);
        return ResponseEntity.ok("Successfully deleted");
    }

    /**
     * PUT endpoint na url adrese /api/court_type/update/{id}
     *
     * @param id id typu kurtu který chceme upravit v databázi
     * @param type nový typ kurtu
     * @param price nová cena daného typu kurtu
     * @return chybový status pokud se pokusíme upravit typ kurtu s neexistujícím id nebo ok status
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCourtType(@PathVariable Integer id,
                                 @RequestParam(required = false) String type,
                                 @RequestParam(required = false) Integer price) {
        if (!courtTypeService.validId(id)) {
            return ResponseEntity.badRequest().body("Invalid id");
        }

        courtTypeService.updateCourtType(id, type, price);
        return ResponseEntity.ok("Successfully updated");
    }
}
