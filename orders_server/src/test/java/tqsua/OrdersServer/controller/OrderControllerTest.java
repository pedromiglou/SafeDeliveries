package tqsua.OrdersServer.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tqsua.OrdersServer.service.OrderService;
import tqsua.OrdersServer.JsonUtil;
import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.model.Order;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
@SpringBootTest
class OrderControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService service;

    @Test
    void whenGetAllOrders_thenReturnResult() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(40.0, 30.0, 40.1, 31.1, "Entregue", 12);
        Order order2 = new Order(41.0, 31.0, 41.1, 32.1, "Entregue", 12);
        response.add(order1);
        response.add(order2);
        given(service.getAllOrders()).willReturn(response);

        mvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).getAllOrders();
        reset(service);
    }

    @Test
    void whenCreateOrder_thenReturnResult() throws Exception {
        Order order1 = new Order(40.0, 30.0, 40.1, 31.1, "PREPROCESSING", 12);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);
        
        given(service.saveOrder(Mockito.any())).willReturn(order1);

        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pick_up_lat", is(order1.getPick_up_lat())))
                .andExpect(jsonPath("$.pick_up_lng", is(order1.getPick_up_lng())))
                .andExpect(jsonPath("$.deliver_lat", is(order1.getDeliver_lat())))
                .andExpect(jsonPath("$.deliver_lng", is(order1.getDeliver_lng())))
                .andExpect(jsonPath("$.status", is(order1.getStatus())))
                .andExpect(jsonPath("$.rating", is(order1.getRating())))
                .andExpect(jsonPath("$.user_id", is(Integer.parseInt(String.valueOf(order1.getUser_id())))))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).saveOrder(Mockito.any());
        reset(service);
    }

}