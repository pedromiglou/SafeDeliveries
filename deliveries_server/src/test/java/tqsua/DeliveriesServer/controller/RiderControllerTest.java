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

        mvc.perform(get("/api/riders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).getAllRiders();
    }

    @Test
    void whenGetRiderById_thenReturnRider() throws Exception {
        Rider response = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        response.setLat(12.0);
        response.setLng(93.0);
        given(service.getRiderById(response.getId())).willReturn(response);

        mvc.perform(get("/api/rider?id="+String.valueOf(response.getId())).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) response.getId())));
        verify(service, VerificationModeFactory.times(1)).getRiderById(response.getId());
    }

    @Test
    void whenGetRiderByInvalidId_thenReturnRider() throws Exception {
        given(service.getRiderById(-1L)).willReturn(null);

        mvc.perform(get("/api/rider?id=-1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).getRiderById(-1);
    }

    @Test
    void whenUpdateRider_thenReturnOk() throws Exception {
        RiderDTO newDetails = new RiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);
        Rider rider = new Rider("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        given(service.updateRider(0L, newDetails)).willReturn(rider);
        //with all arguments
        mvc.perform(put("/api/rider/0").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(0, newDetails);

        //with less arguments
        newDetails = new RiderDTO(null, "B", "a@b.c", null, 5.0, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);
        given(service.updateRider(0L, newDetails)).willReturn(rider);
        mvc.perform(put("/api/rider/0").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(0, newDetails);
    }

    @Test
    void whenUpdateNotExistentRider_thenReturnNotFound() throws Exception {
        RiderDTO newDetails = new RiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);
        given(service.updateRider(0L, newDetails)).willReturn(null);

        mvc.perform(put("/api/rider/0").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).updateRider(0, newDetails);
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
        response.add(order1);
        response.add(order2);

        given(service.updateRider(1L, newDetails)).willReturn(rider);

        given(order_service.getPendingOrders()).willReturn(response);
        response.remove(0);
        given(order_service.getRefusedOrders(1)).willReturn(response);
        
        //with all arguments
        mvc.perform(put("/api/rider/1").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(1, newDetails);
        verify(notification_service, VerificationModeFactory.times(1)).save(Mockito.any());

    }

}