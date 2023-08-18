package com.example.court;

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
@WebMvcTest(CourtController.class)
class CourtControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourtService courtService;

    @Test
    void addCourt() throws Exception {
        String request = objectMapper.writeValueAsString(new Court());
        mockMvc.perform(post("/api/court/add")
                .contentType("application/json")
                .content(request))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/court/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCourts() throws Exception {
        List<Court> fakeCourts = new ArrayList<>();
        fakeCourts.add(new Court());
        fakeCourts.add(new Court());

        when(courtService.getAllCourts()).thenReturn(fakeCourts);

        mockMvc.perform(get("/api/court"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteCourt() throws Exception {
        when(courtService.validId(1)).thenReturn(true);

        mockMvc.perform(delete("/api/court/delete/1"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/court/delete/a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/court/delete/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCourt() throws Exception {
        when(courtService.validId(1)).thenReturn(true);

        mockMvc.perform(put("/api/court/update/1"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/court/update/1?courtTypeId=1"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/court/update/a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/court/update/1?courtTypeId=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/court/update/"))
                .andExpect(status().isNotFound());
    }
}
