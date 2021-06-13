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
import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.model.OrderDTO;
import tqsua.DeliveriesServer.repository.OrderRepository;

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
}
