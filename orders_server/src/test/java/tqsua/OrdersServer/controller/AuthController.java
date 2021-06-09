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
import static org.mockito.Matchers.anyString;

import tqsua.OrdersServer.service.UserService;
import tqsua.OrdersServer.JsonUtil;
import tqsua.OrdersServer.model.User;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void whenRegistWithValidData_thenRegistWithSucess() throws Exception {
        User user = new User("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", "U");

        given(userService.existsUserByEmail(anyString())).willReturn(false);

        mvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("User was registed with sucess!")));
        verify(userService, VerificationModeFactory.times(1)).existsUserByEmail(anyString());
        reset(userService);
    }

    @Test
    void whenRegistWithInvalidEmail_thenReturnErrorMessage() throws Exception {
        User user = new User("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", "U");

        given(userService.existsUserByEmail(anyString())).willReturn(true);

        mvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Email is already in use!")));
        verify(userService, VerificationModeFactory.times(1)).existsUserByEmail(anyString());
        reset(userService);
    }

}