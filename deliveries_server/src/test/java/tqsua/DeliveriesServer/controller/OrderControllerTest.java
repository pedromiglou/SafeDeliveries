package tqsua.DeliveriesServer.controller;


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
import tqsua.DeliveriesServer.JsonUtil;
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

    String token = this.getToken("1");

    String invalidtoken = this.getToken("5");

    @Test
    void whenGetAllOrders_thenReturnResult() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = new Order(0, 37.3, 39.4, 38.2, 39.3, 36.3, "SafeDeliveries");
        response.add(order1);
        response.add(order2);
        Rider rider = createRider("Rafael", "Baptista", "rafael@gmail.com", "1234", 4.0, "Online", "Admin");
        given(rider_service.getRiderById(1)).willReturn(rider);
        given(service.getAllOrders()).willReturn(response);

        mvc.perform(get("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(service, VerificationModeFactory.times(1)).getAllOrders();
        verify(rider_service, VerificationModeFactory.times(1)).getRiderById(1);
        reset(service);
    }

    @Test
    void whenGetAllOrdersWithoutPermission_thenReturnResult() throws Exception {
        Rider rider = createRider("Rafael", "Baptista", "rafael@gmail.com", "1234", 4.0, "Online", "User");
        given(rider_service.getRiderById(1)).willReturn(rider);

        mvc.perform(get("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).getAllOrders();
        verify(rider_service, VerificationModeFactory.times(1)).getRiderById(1);
        reset(service);
    }

    @Test
    void whenGetStatisticsOrders_thenReturnResult() throws Exception {
        Rider rider = createRider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online", "Admin");

        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = new Order(0, 37.3, 39.4, 38.2, 39.3, 36.3, "SafeDeliveries");
        response.add(order1);
        response.add(order2);

        ArrayList<Integer> orderLast7Days = new ArrayList<>();
        orderLast7Days.add(1);
        orderLast7Days.add(2);
        orderLast7Days.add(3);
        orderLast7Days.add(4);
        orderLast7Days.add(5);
        orderLast7Days.add(6);
        orderLast7Days.add(7);

        ArrayList<Integer> orderWeight = new ArrayList<>();
        orderWeight.add(1);
        orderWeight.add(2);
        orderWeight.add(3);
        orderWeight.add(4);

        given(rider_service.getRiderById(1)).willReturn(rider);
        given(service.getTotalOrders()).willReturn(4);
        given(service.countOrders("Pending")).willReturn(2);
        given(service.countOrders("Delivering")).willReturn(1);
        given(service.getOrdersLast7Days()).willReturn(orderLast7Days);
        given(service.getOrdersByWeight()).willReturn(orderWeight);

        mvc.perform(get("/api/private/orders/statistics").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_orders", is(4)))
                .andExpect(jsonPath("$.pending_orders", is(2)))
                .andExpect(jsonPath("$.delivering_orders", is(1)))
                .andExpect(jsonPath("$.completed_orders", is(1)))
                .andExpect(jsonPath("$.orders_7_days", is(orderLast7Days)))
                .andExpect(jsonPath("$.orders_by_weight", is(orderWeight)));

        verify(rider_service, VerificationModeFactory.times(1)).getRiderById(1);
        verify(service, VerificationModeFactory.times(1)).getTotalOrders();
        verify(service, VerificationModeFactory.times(1)).countOrders("Pending");
        verify(service, VerificationModeFactory.times(1)).countOrders("Delivering");
        verify(service, VerificationModeFactory.times(1)).getOrdersLast7Days();
        verify(service, VerificationModeFactory.times(1)).getOrdersByWeight();
        reset(service);
    }

    @Test
    void whenGetStatisticsOrdersWithoutPermissions_thenReturnUnauthorized() throws Exception {
        Rider rider = createRider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online", "User");
        given(rider_service.getRiderById(1)).willReturn(rider);

        mvc.perform(get("/api/private/orders/statistics").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
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
    void whenAcceptOrderWithDifferentToken_thenReturnUnauthorized() throws Exception {
        Order order = new Order(1, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        
        given(service.updateRider(2, 1)).willReturn(order);

        mvc.perform(post("/api/private/acceptorder?order_id=2&rider_id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).updateRider(2, 1);
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

    @Test
    void whenDeclineOrderWithDifferentToken_thenReturnUnauthorized() throws Exception {
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

        mvc.perform(post("/api/private/declineorder?order_id=2&rider_id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).getOrderById(2);
        verify(notification_service, VerificationModeFactory.times(0)).save(Mockito.any());
        reset(service);
        reset(notification_service);
    } 

    public Rider createRider(String firstname, String lastname, String email, String password, double rating, String status, String account_type) {
        Rider rider = new Rider(firstname, lastname, email, password, rating, status);
        rider.setAccountType(account_type);
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