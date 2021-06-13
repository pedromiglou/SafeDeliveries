package tqsua.OrdersServer.integration;

import java.util.HashSet;
import java.util.Set;

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
import tqsua.OrdersServer.model.OrderDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OrdersServerApplication.class)
@AutoConfigureMockMvc
public class OrderControllerIT {
    
    @Autowired
    private MockMvc mvc;

    @Test
    void whenCreatingOrderWithValidParams_thenCreateWithSucess() throws Exception {
        OrderDTO order1 = new OrderDTO(40.0, 30.0, 40.1, 31.1, "PREPROCESSING", 12);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
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
        OrderDTO order1 = new OrderDTO(null, 30.0, 40.1, 31.1, "PREPROCESSING", 12);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
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

        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. No user specified.")));


    }

    @Test
    void whenCreatingOrderWithNoItems_thenReturnErrorMessage() throws Exception {
        OrderDTO order1 = new OrderDTO(40.0, 30.0, 40.1, 31.1, "PREPROCESSING", 0);
        
        mvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(order1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error. Order with 0 items.")));


    }
}
