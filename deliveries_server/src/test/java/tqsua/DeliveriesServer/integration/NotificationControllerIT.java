package tqsua.DeliveriesServer.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tqsua.DeliveriesServer.DeliveriesServerApplication;
import tqsua.DeliveriesServer.JsonUtil;
import tqsua.DeliveriesServer.model.Notification;
import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.model.OrderDTO;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.repository.NotificationRepository;
import tqsua.DeliveriesServer.repository.OrderRepository;
import tqsua.DeliveriesServer.repository.RiderRepository;
import tqsua.DeliveriesServer.security.SecurityConstants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesServerApplication.class)
@AutoConfigureMockMvc
public class NotificationControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private OrderRepository orderRepository;

    String token = "Bearer " + JWT.create()
        .withSubject( "1" )
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
    }

    @Test
    void whenGetNotificationByRiderId_thenReturnResult() throws Exception {
        
        Order order = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        order = orderRepository.save(order);
        Notification notification1 = new Notification(1, order.getOrder_id());
        notificationRepository.save(notification1); 

        mvc.perform(get("/api/private/notifications?id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rider_id", is(1)))
                .andExpect(jsonPath("$.pick_up_lat", is(40.3)))
                .andExpect(jsonPath("$.pick_up_lng", is(30.4)))
                .andExpect(jsonPath("$.deliver_lat", is(41.2)))
                .andExpect(jsonPath("$.deliver_lng", is(31.3)))
                .andExpect(jsonPath("$.weight", is(36.3)));
    }

    @Test
    void whenGetNotificationByInvalidRiderId_thenReturnError() throws Exception {
        token = "Bearer " + JWT.create()
            .withSubject( "-1" )
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        mvc.perform(get("/api/private/notifications?id=-1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetNotificationByInvalidOrderId_thenReturnError() throws Exception {
        Notification notification1 = new Notification(1, -1);
        notificationRepository.save(notification1);
        mvc.perform(get("/api/private/notifications?id=1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isNotFound());
    }
}
