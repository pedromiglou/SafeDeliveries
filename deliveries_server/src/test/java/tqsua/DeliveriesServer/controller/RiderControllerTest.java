package tqsua.DeliveriesServer.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

    String token = this.getToken("1");

    String invalidtoken = this.getToken("5");

    @AfterEach
    void tearDown() {
        reset(service);
    }

    @Test
    void whenGetAllRiders_thenReturnResult() throws Exception {
        ArrayList<Rider> response = new ArrayList<>();
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "Admin");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline", "User");
        response.add(rider1);
        response.add(rider2);
        given(service.getRiderById(1)).willReturn(rider1);
        given(service.getAllRiders()).willReturn(response);

        mvc.perform(get("/api/private/riders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
        .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).getAllRiders();
        verify(service, VerificationModeFactory.times(1)).getRiderById(1);
    }

    @Test
    void whenGetAllRidersWithoutPermission_thenReturnUnauthorized() throws Exception {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        given(service.getRiderById(1)).willReturn(rider1);

        mvc.perform(get("/api/private/riders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).getAllRiders();
        verify(service, VerificationModeFactory.times(1)).getRiderById(1);
    }

    @Test
    void whenGetRidersStatistics_thenReturnResult() throws Exception {
        Rider rider = createRider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online", "Admin");

        given(service.getRiderById(1)).willReturn(rider);
        given(service.getRidersByState("Online")).willReturn(4);
        given(service.getRidersByState("Offline")).willReturn(3);
        given(service.getRidersByState("Delivering")).willReturn(5);

        mvc.perform(get("/api/private/riders/statistics").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_riders", is(12)))
                .andExpect(jsonPath("$.online_riders", is(4)))
                .andExpect(jsonPath("$.offline_riders", is(3)))
                .andExpect(jsonPath("$.delivering_riders", is(5)));
        verify(service, VerificationModeFactory.times(1)).getRiderById(1);        
        verify(service, VerificationModeFactory.times(1)).getRidersByState("Online");
        verify(service, VerificationModeFactory.times(1)).getRidersByState("Offline");
        verify(service, VerificationModeFactory.times(1)).getRidersByState("Delivering");
    }

    @Test
    void whenGetRidersStatisticsWithoutPermissions_thenReturnUnauthorized() throws Exception {
        Rider rider = createRider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online", "User");

        given(service.getRiderById(1)).willReturn(rider);

        mvc.perform(get("/api/private/riders/statistics").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }

    @Test
    void whenGetRiderById_thenReturnRider() throws Exception {
        Rider response = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        response.setId(1);
        given(service.getRiderById(1)).willReturn(response);

        mvc.perform(get("/api/private/rider?id="+1).contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
                .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
        verify(service, VerificationModeFactory.times(1)).getRiderById(1);
    }

    @Test
    void whenGetRiderByIdWithDifferentToken_thenReturnUnauthorized() throws Exception {
        Rider response = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        response.setId(1);
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
        RiderDTO newDetails = createRiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        Rider rider = createRider("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline", "User");
        given(service.updateRider(1, newDetails)).willReturn(rider);
        //with all arguments
        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(1, newDetails);

        //with less arguments
        newDetails = createRiderDTO(null, "B", "a@b.c", null, 5.0, "Offline");
        given(service.updateRider(1, newDetails)).willReturn(rider);
        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(1, newDetails);
    }

    @Test
    void whenUpdateRiderWithDifferentToken_thenReturnUnauthorized() throws Exception {
        RiderDTO newDetails = createRiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        Rider rider = createRider("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline", "User");
        given(service.updateRider(1, newDetails)).willReturn(rider);
        //with all arguments
        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).updateRider(1, newDetails);

        //with less arguments
        newDetails = createRiderDTO(null, "B", "a@b.c", null, 5.0, "Offline");
        given(service.updateRider(1, newDetails)).willReturn(rider);
        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).updateRider(1, newDetails);
    }

    @Test
    void whenUpdateNotExistentRider_thenReturnNotFound() throws Exception {
        RiderDTO newDetails = createRiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        given(service.updateRider(1, newDetails)).willReturn(null);

        mvc.perform(put("/api/private/rider/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).updateRider(1, newDetails);
    }

    @Test
    void whenUpdateRiderStateToOnline_thenSearchForOrder_AndReturnOk() throws Exception {
        RiderDTO newDetails = createRiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Online");
        Rider rider = createRider("A", "B", "a@b.c", "abcdefgh", 5.0, "Online", "User");

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

    public Rider createRider(String firstname, String lastname, String email, String password, double rating, String status, String account_type) {
        Rider rider = new Rider(firstname, lastname, email, password, rating, status);
        rider.setAccountType(account_type);
        rider.setLat(12.0);
        rider.setLng(93.0);
        return rider;
    }

    public RiderDTO createRiderDTO(String firstname, String lastname, String email, String password, Double rating, String status) {
        RiderDTO rider = new RiderDTO(firstname, lastname, email, password, rating, status);
        rider.setLat(12.0);
        rider.setLng(93.0);
        return rider;
    }

    public String getToken(String id) {
        String token = "Bearer " + JWT.create()
            .withSubject( id )
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
        return token;
    }
}