package tqsua.OrdersServer.controller;

import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tqsua.OrdersServer.service.UserService;
import tqsua.OrdersServer.JsonUtil;
import tqsua.OrdersServer.model.User;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

//@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;
    
    @Test
    void whenGetAllUser_thenReturnResult() throws Exception {
        ArrayList<User> response = new ArrayList<>();
        User user1 = new User("Pedro", "Amaral", "pedro@gmail.com", "1234", "U");
        User user2 = new User("Diogo", "Cunha", "cunha@gmail.com", "1234", "A");
        response.add(user1);
        response.add(user2);
        given(service.getAllUsers()).willReturn(response);

        mvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).getAllUsers();
        reset(service);
    }
    
}