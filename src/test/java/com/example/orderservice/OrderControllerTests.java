package com.example.orderservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldSaveFetchAndDeleteOrder() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "restaurantId": 10,
                                  "customerName": "Ravi",
                                  "itemName": "Veg Biryani",
                                  "quantity": 3
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.restaurantId").value(10))
                .andExpect(jsonPath("$.status").value("SAVED"));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Ravi"));

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "Ravi Kumar",
                                  "itemName": "Paneer Biryani",
                                  "quantity": 2,
                                  "status": "UPDATED"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Ravi Kumar"))
                .andExpect(jsonPath("$.status").value("UPDATED"));

        mockMvc.perform(get("/api/orders?restaurantId=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Paneer Biryani"));

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound());
    }
}
