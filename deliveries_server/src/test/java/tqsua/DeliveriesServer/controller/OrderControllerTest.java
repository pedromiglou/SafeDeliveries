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

import tqsua.DeliveriesServer.service.NotificationService;
import tqsua.DeliveriesServer.service.OrderService;
import tqsua.DeliveriesServer.service.RiderService;
import tqsua.DeliveriesServer.JsonUtil;
import tqsua.DeliveriesServer.model.Notification;
import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.model.OrderDTO;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.security.SecurityConstants;

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

    @MockBean
    private NotificationService notification_service;

    @MockBean
    private RiderService rider_service;

    String token = "Bearer " + JWT.create()
        .withSubject( "1" )
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

    @Test
    void whenGetAllOrders_thenReturnResult() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = new Order(0, 37.3, 39.4, 38.2, 39.3, 36.3, "SafeDeliveries");
        response.add(order1);
        response.add(order2);
        given(service.getAllOrders()).willReturn(response);

        mvc.perform(get("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(service, VerificationModeFactory.times(1)).getAllOrders();
        reset(service);
    }


    @Test
    void whenCreateOrder_thenReturnResult() throws Exception {
        Order order = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        OrderDTO order1 = new OrderDTO(40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        
        ArrayList<Rider> response = new ArrayList<>();
        Rider rider = new Rider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online");
        rider.setLat(43.3);
        rider.setLng(32.4);

        Rider rider2 = new Rider("Rafael", "Baptista", "rafael2@ua.pt", "1234", 5.0, "Online");
        rider2.setLat(12.0);
        rider2.setLng(93.0);

        Rider rider3 = new Rider("Rafael", "Baptista", "rafael3@ua.pt", "1234", 5.0, "Online");
        rider3.setLat(40.1);
        rider3.setLng(30.4);

        response.add(rider);
        response.add(rider2);

        given(service.saveOrder(Mockito.any())).willReturn(order);
        given(rider_service.getAvailableRiders(36.3)).willReturn(response);

        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deliver_id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).saveOrder(Mockito.any());
        verify(notification_service, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(service);
        reset(notification_service);
    }

    @Test
    void whenCreateOrderWithInvalidCoords_thenReturnError() throws Exception {
        OrderDTO order1 = new OrderDTO(null, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid coords.")));
        verify(service, VerificationModeFactory.times(0)).saveOrder(Mockito.any());
        reset(service);

        order1 = new OrderDTO(30.4, null, 41.2, 31.3, 36.3, "SafeDeliveries");
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid coords.")));
        verify(service, VerificationModeFactory.times(0)).saveOrder(Mockito.any());
        reset(service);

        order1 = new OrderDTO(30.4, 30.4, null, 31.3, 36.3, "SafeDeliveries");
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid coords.")));
        verify(service, VerificationModeFactory.times(0)).saveOrder(Mockito.any());
        reset(service);

        order1 = new OrderDTO(30.4, 30.4, 41.2, null, 36.3, "SafeDeliveries");
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid coords.")));
        verify(service, VerificationModeFactory.times(0)).saveOrder(Mockito.any());
        reset(service);
    }

    @Test
    void whenCreateOrderWithInvalidWeight_thenReturnError() throws Exception {
        OrderDTO order1 = new OrderDTO(40.3, 30.4, 41.2, 31.3, -1.2, "SafeDeliveries");
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid Weight.")));
        verify(service, VerificationModeFactory.times(0)).saveOrder(Mockito.any());
        reset(service);
    }

    @Test
    void whenCreateOrderWithInvalidAppName_thenReturnError() throws Exception {
        OrderDTO order1 = new OrderDTO(40.3, 30.4, 41.2, 31.3, 30.2, "invalidAppName");
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid App name.")));
        verify(service, VerificationModeFactory.times(0)).saveOrder(Mockito.any());
        reset(service);
    }

    @Test
    void whenAcceptOrder_thenReturnResult() throws Exception {
        Order order = new Order(1, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        
        given(service.updateRider(2, 1)).willReturn(order);

        mvc.perform(post("/api/private/acceptorder?order_id=2&rider_id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(2, 1);
        reset(service);
    } 

    @Test
    void whenDeclineOrder_thenReturnResult() throws Exception {
        Order order = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        order.setRefused_riders(new ArrayList<>());

        ArrayList<Rider> response = new ArrayList<>();
        Rider rider = new Rider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online");
        rider.setLat(46.3);
        rider.setLng(36.4);

        Rider rider2 = new Rider("Rafael", "Baptista", "rafael2@ua.pt", "1234", 5.0, "Online");
        rider2.setLat(12.0);
        rider2.setLng(93.0);

        Rider rider3 = new Rider("Rafael", "Baptista", "rafael2@ua.pt", "1234", 5.0, "Online");
        rider3.setLat(40.3);
        rider3.setLng(30.4);

        response.add(rider);
        response.add(rider2);
        response.add(rider3);

        given(service.getOrderById(2)).willReturn(order);
        given(rider_service.getAvailableRiders(36.3)).willReturn(response);

        mvc.perform(post("/api/private/declineorder?order_id=2&rider_id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).getOrderById(2);
        verify(notification_service, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(service);
        reset(notification_service);
    } 
}