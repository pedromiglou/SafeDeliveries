package tqsua.DeliveriesServer.controller;

import org.junit.jupiter.api.AfterEach;
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
import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.RiderDTO;
import tqsua.DeliveriesServer.security.SecurityConstants;
import tqsua.DeliveriesServer.JsonUtil;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

//@WebMvcTest(RiderController.class)
@AutoConfigureMockMvc
@SpringBootTest
class RiderControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RiderService service;

    @MockBean
    private OrderService order_service;

    @MockBean
    private NotificationService notification_service;

    String token = "Bearer " + JWT.create()
        .withSubject( "1" )
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

    String invalidtoken = "Bearer " + JWT.create()
        .withSubject( "5" )
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

    @AfterEach
    void tearDown() {
        reset(service);
    }

    @Test
    void whenGetAllRiders_thenReturnResult() throws Exception {
        ArrayList<Rider> response = new ArrayList<>();
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        rider1.setLat(12.0);
        rider1.setLng(93.0);
        rider2.setLat(12.0);
        rider2.setLng(93.0);
        response.add(rider1);
        response.add(rider2);
        given(service.getAllRiders()).willReturn(response);

        mvc.perform(get("/api/private/riders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
        .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).getAllRiders();
    }

    @Test
    void whenGetRiderById_thenReturnRider() throws Exception {
        Rider response = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        response.setId(1);
        response.setLat(12.0);
        response.setLng(93.0);
        given(service.getRiderById(1)).willReturn(response);

        mvc.perform(get("/api/private/rider?id="+1).contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
                .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
        verify(service, VerificationModeFactory.times(1)).getRiderById(1);
    }

    @Test
    void whenGetRiderByIdWithDifferentToken_thenReturnUnauthorized() throws Exception {
        Rider response = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        response.setId(1);
        response.setLat(12.0);
        response.setLng(93.0);
        given(service.getRiderById(1)).willReturn(response);

        mvc.perform(get("/api/private/rider?id="+1).contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken )
                .content(JsonUtil.toJson(response)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).getRiderById(1);
    }

    @Test
    void whenGetRiderByInvalidId_thenReturnRider() throws Exception {
        given(service.getRiderById(1)).willReturn(null);

        mvc.perform(get("/api/private/rider?id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).getRiderById(1);
    }

    @Test
    void whenUpdateRider_thenReturnOk() throws Exception {
        RiderDTO newDetails = new RiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);
        Rider rider = new Rider("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        given(service.updateRider(1, newDetails)).willReturn(rider);
        //with all arguments
        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(1, newDetails);

        //with less arguments
        newDetails = new RiderDTO(null, "B", "a@b.c", null, 5.0, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);
        given(service.updateRider(1, newDetails)).willReturn(rider);
        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(1, newDetails);
    }

    @Test
    void whenUpdateRiderWithDifferentToken_thenReturnUnauthorized() throws Exception {
        RiderDTO newDetails = new RiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);
        Rider rider = new Rider("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        given(service.updateRider(1, newDetails)).willReturn(rider);
        //with all arguments
        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).updateRider(1, newDetails);

        //with less arguments
        newDetails = new RiderDTO(null, "B", "a@b.c", null, 5.0, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);
        given(service.updateRider(1, newDetails)).willReturn(rider);
        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).updateRider(1, newDetails);
    }

    @Test
    void whenUpdateNotExistentRider_thenReturnNotFound() throws Exception {
        RiderDTO newDetails = new RiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);
        given(service.updateRider(1, newDetails)).willReturn(null);

        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).updateRider(1, newDetails);
    }

    @Test
    void whenUpdateRiderStateToOnline_thenSearchForOrder_AndReturnOk() throws Exception {
        RiderDTO newDetails = new RiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Online");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);
        Rider rider = new Rider("A", "B", "a@b.c", "abcdefgh", 5.0, "Online");
        rider.setLat(12.0);
        rider.setLng(93.0);

        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = new Order(0, 37.3, 39.4, 38.2, 39.3, 36.3, "SafeDeliveries");
        Order order3 = new Order(0, 12.2, 93.4, 31.2, 31.3, 36.3, "SafeDeliveries");
        response.add(order1);
        response.add(order2);
        response.add(order3);

        given(service.updateRider(1L, newDetails)).willReturn(rider);

        given(order_service.getPendingOrders()).willReturn(response);
        response.remove(0);
        given(order_service.getRefusedOrders(1)).willReturn(response);
        
        //with all arguments
        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(1, newDetails);
        verify(notification_service, VerificationModeFactory.times(1)).save(Mockito.any());

    }

}