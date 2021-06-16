package tqsua.DeliveriesServer.integration;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import tqsua.DeliveriesServer.DeliveriesServerApplication;
import tqsua.DeliveriesServer.JsonUtil;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.repository.RiderRepository;
import tqsua.DeliveriesServer.service.RiderService;

import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesServerApplication.class)
@AutoConfigureMockMvc
class AuthControllerIT {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private RiderService riderService;

    @Autowired
    private RiderRepository riderRepository;


    @BeforeEach 
    public void setUp() {
        Rider rider = createRider("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", 0.0, "Offline", "User");
        riderService.saveRider(rider);
    }

    @AfterEach
    public void tearDown() {
        riderRepository.deleteAll();
    }

    @Test
    void whenRegistWithValidData_thenRegistWithSucess() throws Exception {

        Rider rider = createRider("Filipe", "Carvalho", "filipe@gmail.com", "filipe123", 0.0, "Offline", "User");

        mvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(rider)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("Rider was registed with sucess!")));


    }

    @Test
    void whenRegistWithInvalidEmail_thenReturnErrorMessage() throws Exception {

        Rider rider = createRider("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", 0.0, "Offline", "User");

        mvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.toJson(rider)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Email is already in use!")));


    }

    public Rider createRider(String firstname, String lastname, String email, String password, double rating, String status, String account_type) {
        Rider rider = new Rider(firstname, lastname, email, password, rating, status);
        rider.setAccountType(account_type);
        rider.setLat(12.0);
        rider.setLng(93.0);
        return rider;
    }
}