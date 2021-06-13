package tqsua.DeliveriesServer.controller;


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

import tqsua.DeliveriesServer.service.OrderService;
import tqsua.DeliveriesServer.JsonUtil;
import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.model.OrderDTO;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

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
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = new Order(0, 37.3, 39.4, 38.2, 39.3, 36.3, "SafeDeliveries");
        response.add(order1);
        response.add(order2);
        given(service.getAllOrders()).willReturn(response);

        mvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(service, VerificationModeFactory.times(1)).getAllOrders();
        reset(service);
    }


    @Test
    void whenCreateOrder_thenReturnResult() throws Exception {
        Order order = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        OrderDTO order1 = new OrderDTO(40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        
        given(service.saveOrder(Mockito.any())).willReturn(order);

        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deliver_id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).saveOrder(Mockito.any());
        reset(service);
    }

}