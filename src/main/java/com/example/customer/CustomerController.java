package com.example.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Třída reprezentující kontroler endpointů týkající se zákazníků
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * POST endpoint na url adrese /api/customer/add
     *
     * @param customer zákazník kterého chceme přidat do databáze
     */
    @PostMapping("/add")
    public void addCustomer(@RequestBody Customer customer) {
        customerService.addCustomer(customer);
    }

    /**
     * GET endpoint na url adrese /api/customer
     *
     * @return list všech nesmazaných zákazníků v databázi
     */
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * DELETE endpoint na url adrese /api/customer/delete/{id}
     *
     * @param id id zákazníka kterého chceme smazat z databáze
     * @return chybový status pokud se pokusíme smazat zákazníka s neexistujícím id nebo ok status
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer id) {
        if (!customerService.validId(id)) {
            return ResponseEntity.badRequest().body("Invalid id");
        }

        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Successfully deleted");
    }

    /**
     * PUT endpoint na url adrese /api/customer/update/{id}
     *
     * @param id id zákazníka kterého chceme upravit v databázi
     * @param phone nové telefonní číslo zákazníka
     * @param name nové jméno zákazníka
     * @return chybový status pokud se pokusíme upravit zákazníka s neexistujícím id nebo ok status
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Integer id,
                                                 @RequestParam(required = false) Integer phone,
                                                 @RequestParam(required = false) String name) {
        if (!customerService.validId(id)) {
            return ResponseEntity.badRequest().body("Invalid id");
        }

        customerService.updateCustomer(id, phone, name);
        return ResponseEntity.ok("Successfully updated");
    }
}
