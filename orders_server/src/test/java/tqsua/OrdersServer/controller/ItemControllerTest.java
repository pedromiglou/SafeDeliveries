package tqsua.OrdersServer.controller;

import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tqsua.OrdersServer.service.ItemService;
import tqsua.OrdersServer.JsonUtil;
import tqsua.OrdersServer.model.Item;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

//@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@SpringBootTest
class ItemControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService service;

    
    @Test
    void whenGetAllItems_thenReturnResult() throws Exception {
        ArrayList<Item> response = new ArrayList<>();
        Item item1 = new Item("TV", "Informatica", 43.0);
        Item item2 = new Item("Frigorifico", "Eletrodomesticos", 290.6);
        response.add(item1);
        response.add(item2);
        given(service.getAllItems()).willReturn(response);

        mvc.perform(get("/api/items").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).getAllItems();
        reset(service);
    }
    
}