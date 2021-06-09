package tqsua.OrdersServer.integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import tqsua.OrdersServer.OrdersServerApplication;
import tqsua.OrdersServer.JsonUtil;
import tqsua.OrdersServer.model.User;
import tqsua.OrdersServer.service.UserService;

import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = OrdersServerApplication.class)
@AutoConfigureMockMvc
class AuthControllerIT {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;


    @BeforeEach 
    public void setUp() {
        User user = new User("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", "U");
        userService.saveUser(user);
    }

    @Test
    void whenRegistWithValidData_thenRegistWithSucess() throws Exception {

        User user = new User("Filipe", "Carvalho", "filipe@gmail.com", "filipe123", "U");

        mvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("User was registed with sucess!")));


    }


    @Test
    void whenRegistWithInvalidEmail_thenReturnErrorMessage() throws Exception {

        User user = new User("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", "U");

        mvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Email is already in use!")));


    }

}