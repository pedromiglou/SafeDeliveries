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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    void whenGetAllOrders_thenReturnResult() throws Exception {
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = new Order(0, 41.3, 32.4, 42.2, 32.3, 13.4, "SafeDeliveries");
        orderRepository.save(order1);
        orderRepository.save(order2);

        mvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
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
        Order order = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        order = orderRepository.save(order);
        rider = riderRepository.save(rider);
        Notification notification = new Notification(rider.getId(), order.getOrder_id());
        notificationRepository.save(notification);

        mvc.perform(post("/api/acceptorder?order_id=" + order.getOrder_id() + "&rider_id=" + rider.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    } 

    @Test
    void whenDeclineOrder_thenReturnResult() throws Exception {
        Order order = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        order = orderRepository.save(order);
        rider = riderRepository.save(rider);
        Notification notification = new Notification(rider.getId(), order.getOrder_id());
        notificationRepository.save(notification);

        mvc.perform(post("/api/acceptorder?order_id=" + order.getOrder_id() + "&rider_id=" + rider.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    } 
}
