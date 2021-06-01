package tqsua.OrdersServer.controller;

import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tqsua.OrdersServer.service.OrderService;
import tqsua.OrdersServer.model.Order;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService service;

    @Test
    void whenGetAllOrders_thenReturnResult() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order("Rua das Laranjeiras", "Rua Lourenço Peixinho", new Date(), "Entregue", 4);
        Order order2 = new Order("Rua da Universidade", "Bairro de Santiago", new Date(), "Entregue", 5);
        response.add(order1);
        response.add(order2);
        given(service.getAllOrders()).willReturn(response);

        mvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).getAllOrders();
        reset(service);
    }

}