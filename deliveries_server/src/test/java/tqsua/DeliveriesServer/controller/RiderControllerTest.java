package tqsua.DeliveriesServer.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tqsua.DeliveriesServer.service.RiderService;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.JsonUtil;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;

//@WebMvcTest(RiderController.class)
@AutoConfigureMockMvc
@SpringBootTest
class RiderControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RiderService service;

    @AfterEach
    void tearDown() {
        reset(service);
    }

    @Test
    void whenGetAllRiders_thenReturnResult() throws Exception {
        ArrayList<Rider> response = new ArrayList<>();
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        response.add(rider1);
        response.add(rider2);
        given(service.getAllRiders()).willReturn(response);

        mvc.perform(get("/api/riders").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).getAllRiders();
    }

    @Test
    void whenGetRiderById_thenReturnRider() throws Exception {
        Rider response = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        given(service.getRiderById(response.getId())).willReturn(response);

        mvc.perform(get("/api/rider?id="+String.valueOf(response.getId())).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) response.getId())));
        verify(service, VerificationModeFactory.times(1)).getRiderById(response.getId());
    }

    @Test
    void whenGetRiderByInvalidId_thenReturnRider() throws Exception {
        given(service.getRiderById(-1L)).willReturn(null);

        mvc.perform(get("/api/rider?id=-1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).getRiderById(-1);
    }

    @Test
    void whenUpdateRider_thenCorrectlyCallService() throws Exception {
        //with all arguments
        mvc.perform(put("/api/rider?id=0&firstname=A&lastname=B&email=a@b.c&password=abcdefgh&status=Offline&rating=5")).andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(0,"A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");

        //with less arguments
        mvc.perform(put("/api/rider?id=0&lastname=B&email=a@b.c&status=Offline&rating=5")).andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateRider(0,null, "B", "a@b.c", null, 5.0, "Offline");
    }
}