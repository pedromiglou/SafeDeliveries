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
import tqsua.DeliveriesServer.repository.VehicleRepository;
import tqsua.DeliveriesServer.security.SecurityConstants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesServerApplication.class)
@AutoConfigureMockMvc
public class OrderControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    String token = this.getToken("1");

    String invalidtoken = this.getToken("5");

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
        orderRepository.deleteAll();
        riderRepository.deleteAll();
    }

    @Test
    void whenGetAllOrders_thenReturnResult() throws Exception {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "Admin");
        riderRepository.save(rider1);
        Order order1 = createOrder(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = createOrder(0, 41.3, 32.4, 42.2, 32.3, 13.4, "SafeDeliveries");
        orderRepository.save(order1);
        orderRepository.save(order2);

        token = getToken(String.valueOf(rider1.getId()));

        mvc.perform(get("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void whenGetAllOrdersWithoutPermission_thenReturnUnauthorized() throws Exception {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        riderRepository.save(rider1);

        token = getToken(String.valueOf(rider1.getId()));

        mvc.perform(get("/api/private/orders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }


    @Test
    void whenGetStatisticsOrders_thenReturnResult() throws Exception {
        Rider rider = createRider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online", "Admin");
        riderRepository.save(rider);

        token = getToken(String.valueOf(rider.getId()));

        // 2 pending orders
        Order order1 = createOrder(0, 40.3, 30.4, 41.2, 31.3, 1.0, "SafeDeliveries");
        Order order2 = createOrder(0, 37.3, 39.4, 38.2, 39.3, 5.3, "SafeDeliveries");
        orderRepository.save(order1);
        orderRepository.save(order2);

        // 3 delivering orders
        Order order3 = new Order(1, 40.3, 30.4, 41.2, 31.3, 18.3, "SafeDeliveries");
        order3.setStatus("Delivering");
        Order order4 = new Order(1, 37.3, 39.4, 38.2, 39.3, 36.3, "SafeDeliveries");
        order4.setStatus("Delivering");
        Order order5 = new Order(1, 37.3, 39.4, 38.2, 39.3, 50.3, "SafeDeliveries");
        order5.setStatus("Delivering");
        orderRepository.save(order3);
        orderRepository.save(order4);
        orderRepository.save(order5);

        // 2 completed orders
        Order order6 = new Order(1, 40.3, 30.4, 41.2, 31.3, 18.3, "SafeDeliveries");
        order6.setStatus("Completed");
        Order order7 = new Order(1, 37.3, 39.4, 38.2, 39.3, 36.3, "SafeDeliveries");
        order7.setStatus("Completed");
        orderRepository.save(order6);
        orderRepository.save(order7);

        ArrayList<Integer> orderLast7Days = new ArrayList<>();
        orderLast7Days.add(0);
        orderLast7Days.add(0);
        orderLast7Days.add(0);
        orderLast7Days.add(0);
        orderLast7Days.add(0);
        orderLast7Days.add(0);
        orderLast7Days.add(7);

        ArrayList<Integer> orderWeight = new ArrayList<>();
        orderWeight.add(1);
        orderWeight.add(1);
        orderWeight.add(2);
        orderWeight.add(3);

        mvc.perform(get("/api/private/orders/statistics").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_orders", is(7)))
                .andExpect(jsonPath("$.pending_orders", is(2)))
                .andExpect(jsonPath("$.delivering_orders", is(3)))
                .andExpect(jsonPath("$.completed_orders", is(2)))
                .andExpect(jsonPath("$.orders_7_days", is(orderLast7Days)))
                .andExpect(jsonPath("$.orders_by_weight", is(orderWeight)));

    }

    @Test
    void whenGetStatisticsOrdersWithoutPermissions_thenReturnUnauthorized() throws Exception {
        Rider rider = createRider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online", "User");
        riderRepository.save(rider);

        token = getToken(String.valueOf(rider.getId()));

        mvc.perform(get("/api/private/orders/statistics").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }


    @Test
    void whenCreatingOrderWithValidParams_thenCreateWithSucess() throws Exception {
        OrderDTO order1 = new OrderDTO(40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(order1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deliver_id").isNotEmpty());
    }

    @Test
    void whenCreatingOrderWithInvalidWeight_thenThrowError() throws Exception {
        OrderDTO order1 = new OrderDTO(40.3, 30.4, 41.2, 31.3, -1.0, "SafeDeliveries");
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid Weight.")));
    }

    @Test
    void whenCreatingOrderWithInvalidCoords_thenThrowError() throws Exception {
        OrderDTO order1 = new OrderDTO(null, 30.4, 41.2, 31.3, 36.0, "SafeDeliveries");
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid coords.")));
    }

    @Test
    void whenCreatingOrderWithInvalidAppName_thenThrowError() throws Exception {
        OrderDTO order1 = new OrderDTO(40.3, 30.4, 41.2, 31.3, 36.0, "InvalidAppName");
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid App name.")));
    }

    @Test
    void whenAcceptOrder_thenReturnResult() throws Exception {
        Order order = createOrder(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        order = orderRepository.save(order);
        rider = riderRepository.save(rider);
        Notification notification = new Notification(rider.getId(), order.getOrder_id());
        notificationRepository.save(notification);

        token = getToken(String.valueOf(rider.getId()));

        mvc.perform(post("/api/private/acceptorder?order_id=" + order.getOrder_id() + "&rider_id=" + rider.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token ))
                .andExpect(status().isOk());
    }
    
    @Test
    void whenDeclineOrder_thenReturnResult() throws Exception {
        Order order = createOrder(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        order = orderRepository.save(order);
        rider = riderRepository.save(rider);
        Notification notification = new Notification(rider.getId(), order.getOrder_id());
        notificationRepository.save(notification);

        token = getToken(String.valueOf(rider.getId()));

        mvc.perform(post("/api/private/declineorder?order_id=" + order.getOrder_id() + "&rider_id=" + rider.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token ))
                .andExpect(status().isOk());
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

    public Order createOrder(int rider_id, Double pick_up_lat, Double pick_up_lng, Double deliver_lat, Double deliver_lng, Double weight, String app_name) {
        Order o1 = new Order(rider_id, pick_up_lat, pick_up_lng, deliver_lat, deliver_lng, weight, app_name);
        o1.setStatus("Pending");
        return o1;
    }
}
