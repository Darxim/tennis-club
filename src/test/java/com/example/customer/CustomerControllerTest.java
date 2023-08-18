package com.example.customer;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void addCustomer() throws Exception {
        String request = objectMapper.writeValueAsString(new Customer());
        mockMvc.perform(post("/api/customer/add")
                .contentType("application/json")
                .content(request))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/customer/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCustomers() throws Exception {
        List<Customer> fakeCustomers = new ArrayList<>();
        fakeCustomers.add(new Customer());
        fakeCustomers.add(new Customer());

        when(customerService.getAllCustomers()).thenReturn(fakeCustomers);

        mockMvc.perform(get("/api/customer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteCustomer() throws Exception {
        when(customerService.validId(1)).thenReturn(true);

        mockMvc.perform(delete("/api/customer/delete/1"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/customer/delete/a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/customer/delete/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomer() throws Exception {
        when(customerService.validId(1)).thenReturn(true);

        mockMvc.perform(put("/api/customer/update/1"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/customer/update/1?name=Karel Novotný"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/customer/update/1?name=Karel Novotný&phone=123456789"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/customer/update/1?phone=123456789"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/customer/update/a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/customer/update/1?name=Karel Novotný&phone=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/customer/update/1?phone=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/customer/update/"))
                .andExpect(status().isNotFound());
    }
}
