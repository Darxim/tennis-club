package com.example.reservation;

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
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @Test
    void addReservation() throws Exception {
        Reservation reservation = new Reservation();
        String request = objectMapper.writeValueAsString(reservation);

        when(reservationService.validReservation(reservation)).thenReturn(true);

        mockMvc.perform(post("/api/reservation/add")
                .contentType("application/json")
                .content(request))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/reservation/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllReservations() throws Exception {
        List<Reservation> fakeReservations = new ArrayList<>();
        fakeReservations.add(new Reservation());
        fakeReservations.add(new Reservation());

        when(reservationService.getAllReservations()).thenReturn(fakeReservations);

        mockMvc.perform(get("/api/reservation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteReservation() throws Exception {
        when(reservationService.validId(1)).thenReturn(true);

        mockMvc.perform(delete("/api/reservation/delete/1"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/reservation/delete/a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/reservation/delete/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateReservation() throws Exception {
        when(reservationService.validId(1)).thenReturn(true);

        mockMvc.perform(put("/api/reservation/update/1"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/reservation/update/1?customerName=Karel Novotný"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/reservation/update/1?customerName=Karel Novotný&customerPhone=123456789"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/reservation/update/1?customerPhone=123456789"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/reservation/update/1?courtId=2"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/reservation/update/1?reservationDate=11-09-2023"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/reservation/update/1?fromTime=13:00"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/reservation/update/1?toTime=14:30"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/reservation/update/1?gameType=TWO_PLAYERS"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/reservation/update/1?gameType=FOUR_PLAYERS"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/reservation/update/a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/reservation/update/1?customerName=Karel Novotný&customerPhone=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/reservation/update/1?customerPhone=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/reservation/update/1?gameType=THREE_PLAYERS"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/reservation/update/1?reservationDate=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/reservation/update/1?fromTime=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/reservation/update/1?toTime=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/reservation/update/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllReservationsByCourtId() throws Exception {
        mockMvc.perform(get("/api/reservation/1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/reservation/a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/reservation/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllReservationsByCustomerPhone() throws Exception {
        mockMvc.perform(get("/api/reservation/phone/123456789"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/reservation/phone/123456789?future=true"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/reservation/phone/a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/reservation/phone/123456789?future=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/reservation/phone/123456789?future=2"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/reservation/phone/"))
                .andExpect(status().isNotFound());
    }
}
