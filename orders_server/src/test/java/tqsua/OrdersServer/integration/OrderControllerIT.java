package tqsua.OrdersServer.integration;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.*;
import tqsua.OrdersServer.JsonUtil;
import tqsua.OrdersServer.OrdersServerApplication;
import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.model.Order;
import tqsua.OrdersServer.model.OrderDTO;
import tqsua.OrdersServer.repository.ItemRepository;
import tqsua.OrdersServer.repository.OrderRepository;
import tqsua.OrdersServer.security.SecurityConstants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OrdersServerApplication.class)
@AutoConfigureMockMvc
public class OrderControllerIT {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    String token = "Bearer " + JWT.create()
        .withSubject( "1" )
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Disabled("Unable to run in CI")
    @Test
    void whenCreatingOrderWithValidParams_thenCreateWithSucess() throws Exception {
        OrderDTO order1 = new OrderDTO(40.0, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);
        
        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token )
                .content(JsonUtil.toJson(order1)))
                .andExpect(jsonPath("$.pick_up_lat", is(order1.getPick_up_lat())))
                .andExpect(jsonPath("$.pick_up_lng", is(order1.getPick_up_lng())))
                .andExpect(jsonPath("$.deliver_lat", is(order1.getDeliver_lat())))
                .andExpect(jsonPath("$.deliver_lng", is(order1.getDeliver_lng())))
                .andExpect(jsonPath("$.status", is(order1.getStatus())))
                .andExpect(jsonPath("$.rating", is(order1.getRating())))
                .andExpect(jsonPath("$.user_id", is(Integer.parseInt(String.valueOf(order1.getUser_id())))))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void whenCreatingOrderWithInvalidCoords_thenReturnErrorMessage() throws Exception {
        OrderDTO order1 = new OrderDTO(null, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token )
                .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Invalid coords.")));


    }

    @Test
    void whenCreatingOrderWithNoUser_thenReturnErrorMessage() throws Exception {
        OrderDTO order1 = new OrderDTO(40.0, 30.0, 40.1, 31.1, "PREPROCESSING", 0);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token )
                .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. No user specified.")));


    }

    @Test
    void whenCreatingOrderWithNoItems_thenReturnErrorMessage() throws Exception {
        OrderDTO order1 = new OrderDTO(40.0, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        
        mvc.perform(post("/api/private/orders").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token )
                .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Order with 0 items.")));


    }

    @Test
    void whenNotificateOrder_thenReturnOk() throws Exception {
        String body = "{\"order_id\": 3}";
        Order order1 = new Order(40.2, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        order1.setDeliver_id(3);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        orderRepository.save(order1);

        mvc.perform(post("/api/orders/notificate").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
                .content(body))
                .andExpect(status().isOk());
    }

    @Test
    void whenNotificateNotExistentOrder_thenReturnBadRequest() throws Exception {
        String body = "{\"order_id\": -1}";

        mvc.perform(post("/api/orders/notificate").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error.")));
    }

    @Test
    void whenGetOrderByDeliverId_thenReturnOk() throws Exception {
        Order order1 = new Order(40.2, 30.0, 40.1, 31.1, "PREPROCESSING", 1);
        order1.setDeliver_id(34);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        orderRepository.save(order1);

        mvc.perform(get("/api/orders/34").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pick_up_lat", is(40.2)))
                .andExpect(jsonPath("$.pick_up_lng", is(30.0)))
                .andExpect(jsonPath("$.deliver_lat", is(40.1)))
                .andExpect(jsonPath("$.deliver_lng", is(31.1)))
                .andExpect(jsonPath("$.status", is("PREPROCESSING")));
    }

    @Test
    void whenGetOrderByDeliverIdNotExistent_thenReturnNotFound() throws Exception {

        mvc.perform(get("/api/orders/-1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Not found")));
    }

    @Test
    void whenGetOrderByDeliverIdInvalidToken_thenReturnNotFound() throws Exception {
        Order order1 = new Order(40.2, 30.0, 40.1, 31.1, "PREPROCESSING", 2);
        order1.setDeliver_id(34);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        orderRepository.save(order1);

        mvc.perform(get("/api/orders/34").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }
}
