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
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.repository.RiderRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesServerApplication.class)
@AutoConfigureMockMvc
public class RiderControllerIT {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private RiderRepository riderRepository;

    @BeforeEach
    void setUp() {
        riderRepository.deleteAll();
    }
    @Test
    void whenGetAllRiders_thenReturnResult() throws Exception {
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        riderRepository.save(rider1);
        riderRepository.save(rider2);

        mvc.perform(get("/api/riders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
    }

    @Test
    void whenGetRiderById_thenReturnRider() throws Exception {
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        riderRepository.save(rider1);

        mvc.perform(get("/api/rider?id="+String.valueOf(rider1.getId())).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(rider1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) rider1.getId())));

    }

    @Test
    void whenGetRiderByInvalidId_thenReturnRider() throws Exception {

        mvc.perform(get("/api/rider?id=-1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    /*
    @Test
    void whenUpdateRider_thenCorrectlyCallService() throws Exception {
        //with all arguments
        mvc.perform(put("/api/rider?id=0&firstname=A&lastname=B&email=a@b.c&password=abcdefgh&status=Offline&rating=5"))
            .andExpect(status().isOk());

        //with less arguments
        mvc.perform(put("/api/rider?id=0&lastname=B&email=a@b.c&status=Offline&rating=5"))
            .andExpect(status().isOk());
    }
    */
}
