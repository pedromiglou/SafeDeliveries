package tqsua.DeliveriesServer.controller;


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

import tqsua.DeliveriesServer.service.RiderService;
import tqsua.DeliveriesServer.JsonUtil;
import tqsua.DeliveriesServer.model.Rider;

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
    private RiderService riderService;

    @Test
    void whenRegistWithValidData_thenRegistWithSucess() throws Exception {
        Rider rider = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", 0.0, "Offline", 12.0, 93.0);

        given(riderService.existsRiderByEmail(anyString())).willReturn(false);

        mvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(rider)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Rider was registed with sucess!")));
        verify(riderService, VerificationModeFactory.times(1)).existsRiderByEmail(anyString());
        reset(riderService);
    }

    @Test
    void whenRegistWithInvalidEmail_thenReturnErrorMessage() throws Exception {
        Rider rider = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", 0.0, "Offline", 12.0, 93.0);

        given(riderService.existsRiderByEmail(anyString())).willReturn(true);

        mvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(rider)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Email is already in use!")));
        verify(riderService, VerificationModeFactory.times(1)).existsRiderByEmail(anyString());
        reset(riderService);
    }

}