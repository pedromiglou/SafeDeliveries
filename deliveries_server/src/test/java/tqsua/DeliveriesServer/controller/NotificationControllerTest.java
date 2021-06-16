package tqsua.DeliveriesServer.controller;


import org.junit.jupiter.api.BeforeEach;
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
import tqsua.DeliveriesServer.JsonUtil;
import tqsua.DeliveriesServer.model.Notification;
import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.model.OrderDTO;
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
class NotificationControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private NotificationService service;

    @MockBean
    private OrderService order_service;

    
    String token = "Bearer " + JWT.create()
        .withSubject( "1" )
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

    String invalidtoken = "Bearer " + JWT.create()
        .withSubject( "5" )
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
    

    @Test
    void whenGetNotificationByRiderId_thenReturnResult() throws Exception {
        Notification notification1 = new Notification(1, 2);
        Order order = new Order(1, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        given(service.getNotificationByRider(1)).willReturn(notification1);
        given(order_service.getOrderById(2)).willReturn(order);

        mvc.perform(get("/api/private/notifications?id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rider_id", is(1)))
                .andExpect(jsonPath("$.pick_up_lat", is(40.3)))
                .andExpect(jsonPath("$.pick_up_lng", is(30.4)))
                .andExpect(jsonPath("$.deliver_lat", is(41.2)))
                .andExpect(jsonPath("$.deliver_lng", is(31.3)))
                .andExpect(jsonPath("$.weight", is(36.3)));
        verify(service, VerificationModeFactory.times(1)).getNotificationByRider(1);
        reset(service);
    }

    @Test
    void whenGetNotificationByRiderIdDifferentToken_thenReturnResult() throws Exception {
        Notification notification1 = new Notification(1, 2);
        Order order = new Order(1, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        given(service.getNotificationByRider(1)).willReturn(notification1);
        given(order_service.getOrderById(2)).willReturn(order);

        mvc.perform(get("/api/private/notifications?id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).getNotificationByRider(1);
        reset(service);
    }

    @Test
    void whenGetNotificationByInvalidRiderId_thenReturnError() throws Exception {
        Order order = new Order(1, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        given(service.getNotificationByRider(1)).willReturn(null);
        given(order_service.getOrderById(2)).willReturn(order);

        mvc.perform(get("/api/private/notifications?id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).getNotificationByRider(1);
        reset(service);
    }

    @Test
    void whenGetNotificationByInvalidOrderId_thenReturnError() throws Exception {
        Notification notification1 = new Notification(1, 2);
        given(service.getNotificationByRider(1)).willReturn(notification1);
        given(order_service.getOrderById(2)).willReturn(null);

        mvc.perform(get("/api/private/notifications?id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).getNotificationByRider(1);
        reset(service);
    }
}