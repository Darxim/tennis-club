package com.example.court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Třída reprezentující kontroler endpointů týkající se kurtů
 */
@RestController
@RequestMapping("/api/court")
public class CourtController {
    private final CourtService courtService;

    @Autowired
    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    /**
     * POST endpoint na url adrese /api/court/add
     *
     * @param court kurt který chceme přidat do databáze
     */
    @PostMapping("/add")
    public void addCourt(@RequestBody Court court) {
        courtService.addCourt(court);
    }

    /**
     * GET endpoint na url adrese /api/court
     *
     * @return list všech nesmazaných kurtů v databázi
     */
    @GetMapping
    public List<Court> getAllCourts() {
        return courtService.getAllCourts();
    }

    /**
     * DELETE endpoint na url adrese /api/court/delete/{id}
     *
     * @param id id kurtu který chceme smazat z databáze
     * @return chybový status pokud se pokusíme smazat kurt s neexistujícím id nebo ok status
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourt(@PathVariable Integer id) {
        if (!courtService.validId(id)) {
            return ResponseEntity.badRequest().body("Invalid id");
        }

        courtService.deleteCourt(id);
        return ResponseEntity.ok("Successfully deleted");
    }

    /**
     * PUT endpoint na url adrese /api/court/update/{id}
     *
     * @param id id kurtu který chceme upravit v databázi
     * @param courtTypeId nové id typu kurtu
     * @return chybový status pokud se pokusíme upravit kurt s neexistujícím id nebo ok status
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCourt(@PathVariable Integer id,
                                              @RequestParam(required = false) Integer courtTypeId) {
        if (!courtService.validId(id)) {
            return ResponseEntity.badRequest().body("Invalid id");
        }

        courtService.updateCourt(id, courtTypeId);
        return ResponseEntity.ok("Successfully updated");
    }
}
