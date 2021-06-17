package tqsua.OrdersServer.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tqsua.OrdersServer.service.OrderService;
import tqsua.OrdersServer.JsonUtil;
import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.model.Order;
import tqsua.OrdersServer.model.OrderDTO;
import tqsua.OrdersServer.security.SecurityConstants;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

//@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
@SpringBootTest
class OrderControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService service;

    String token = "Bearer " + JWT.create()
        .withSubject( "1" )
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

    String invalidtoken = "Bearer " + JWT.create()
        .withSubject( "5" )
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

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
        Order order = new Order(40.0, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        OrderDTO order1 = new OrderDTO(40.0, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order.setItems(items);
        order1.setItems(items);
        
        given(service.saveOrder(Mockito.any())).willReturn(order);
        given(service.deliveryRequest(Mockito.any(), Mockito.any())).willReturn("1");

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pick_up_lat", is(order.getPick_up_lat())))
                .andExpect(jsonPath("$.pick_up_lng", is(order.getPick_up_lng())))
                .andExpect(jsonPath("$.deliver_lat", is(order.getDeliver_lat())))
                .andExpect(jsonPath("$.deliver_lng", is(order.getDeliver_lng())))
                .andExpect(jsonPath("$.status", is(order.getStatus())))
                .andExpect(jsonPath("$.rating", is(order.getRating())))
                .andExpect(jsonPath("$.user_id", is(Integer.parseInt(String.valueOf(order.getUser_id())))))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).saveOrder(Mockito.any());
        reset(service);
    }

    @Test
    void whenCreateOrderWithDifferentToken_thenReturnUnauthorized() throws Exception {
        Order order = new Order(40.0, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        OrderDTO order1 = new OrderDTO(40.0, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order.setItems(items);
        order1.setItems(items);
        
        given(service.saveOrder(Mockito.any())).willReturn(order);
        given(service.deliveryRequest(Mockito.any(), Mockito.any())).willReturn("1");

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken )
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).saveOrder(Mockito.any());
        reset(service);
    }

    @Test
    void whenCreateOrderWithInvalidCoords_thenReturnError() throws Exception {
        OrderDTO order1 = new OrderDTO(null, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid coords.")));
        reset(service);

        order1 = new OrderDTO(40.2, null, 40.1, 31.1, "PREPROCESSING", 12);
        order1.setItems(items);

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid coords.")));
        reset(service);

        order1 = new OrderDTO(40.2, 43.3, null, 31.1, "PREPROCESSING", 12);
        order1.setItems(items);

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid coords.")));
        reset(service);

        order1 = new OrderDTO(40.2, 34.2, 40.1, null, "PREPROCESSING", 12);
        order1.setItems(items);

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid coords.")));
        reset(service);
    }

    @Test
    void whenCreateOrderWithNoItems_thenReturnError() throws Exception {
        OrderDTO order1 = new OrderDTO(30.2, 30.0, 40.1, 31.1, "PREPROCESSING", 1);

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Order with 0 items.")));
        reset(service);
    }

    @Test
    void whenCreateOrderWithInvalidUser_thenReturnError() throws Exception {
        OrderDTO order1 = new OrderDTO(40.3, 30.0, 40.1, 31.1, "PREPROCESSING", 0);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. No user specified.")));
        reset(service);
    }

    @Test
    void whenCreateOrderWithErrorConnectionDeliveries_thenReturnError() throws Exception {
        OrderDTO order1 = new OrderDTO(40.2, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        given(service.deliveryRequest(Mockito.any(), Mockito.any())).willReturn(null);

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Some error occured while connecting to Safe Deliveries.")));
        verify(service, VerificationModeFactory.times(1)).deliveryRequest(Mockito.any(), Mockito.any());
        reset(service);
    }

}