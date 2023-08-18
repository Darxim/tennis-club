package com.example.courtType;

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
@WebMvcTest(CourtTypeController.class)
class CourtTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourtTypeService courtTypeService;

    @Test
    void addCourtType() throws Exception {
        String request = objectMapper.writeValueAsString(new CourtType());
        mockMvc.perform(post("/api/court_type/add")
                .contentType("application/json")
                .content(request))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/court_type/add"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCourtTypes() throws Exception {
        List<CourtType> fakeCourtTypes = new ArrayList<>();
        fakeCourtTypes.add(new CourtType());
        fakeCourtTypes.add(new CourtType());

        when(courtTypeService.getAllCourtTypes()).thenReturn(fakeCourtTypes);

        mockMvc.perform(get("/api/court_type"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteCourtType() throws Exception {
        when(courtTypeService.validId(1)).thenReturn(true);

        mockMvc.perform(delete("/api/court_type/delete/1"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/court_type/delete/a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(delete("/api/court_type/delete/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCourtType() throws Exception {
        when(courtTypeService.validId(1)).thenReturn(true);

        mockMvc.perform(put("/api/court_type/update/1"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/court_type/update/1?type=Antukový povrch"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/court_type/update/1?type=Antukový povrch&price=5"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/court_type/update/1?price=5"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/api/court_type/update/a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/court_type/update/1?type=Antukový povrch&price=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/court_type/update/1?price=a"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/api/court_type/update/"))
                .andExpect(status().isNotFound());
    }
}
