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
import tqsua.DeliveriesServer.model.RiderDTO;
import tqsua.DeliveriesServer.repository.RiderRepository;
import tqsua.DeliveriesServer.repository.VehicleRepository;
import tqsua.DeliveriesServer.security.SecurityConstants;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesServerApplication.class)
@AutoConfigureMockMvc
public class RiderControllerIT {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    String token = "Bearer " + JWT.create()
        .withSubject( "1" )
        .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
        riderRepository.deleteAll();
    }
    
    @Test
    void whenGetAllRiders_thenReturnResult() throws Exception {
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        rider1.setLat(12.0);
        rider1.setLng(93.0);
        Rider rider2 = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        rider2.setLat(12.0);
        rider2.setLng(93.0);
        riderRepository.save(rider1);
        riderRepository.save(rider2);

        mvc.perform(get("/api/private/riders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
    }

    @Test
    void whenGetRiderById_thenReturnRider() throws Exception {
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        rider1.setLat(12.0);
        rider1.setLng(93.0);
        rider1 = riderRepository.save(rider1);

        String updatedtoken = "Bearer " + JWT.create()
            .withSubject( String.valueOf(rider1.getId()) )
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        mvc.perform(get("/api/private/rider?id="+String.valueOf(rider1.getId())).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", updatedtoken )
                .content(JsonUtil.toJson(rider1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) rider1.getId())));

    }

    @Test
    void whenGetRiderByInvalidId_thenReturnRider() throws Exception {
        String updatedtoken = "Bearer " + JWT.create()
            .withSubject( "-1" )
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        mvc.perform(get("/api/private/rider?id=-1").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", updatedtoken ))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdateRider_thenReturnOk() throws Exception {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        rider = riderRepository.save(rider);
        RiderDTO newDetails = new RiderDTO("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);

        String updatedtoken = "Bearer " + JWT.create()
            .withSubject( String.valueOf(rider.getId()) )
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        //with all arguments
        mvc.perform(put("/api/private/rider/"+String.valueOf(rider.getId())).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(newDetails))
                .header("Authorization", updatedtoken ))
                .andExpect(status().isOk());

        //with less arguments
        newDetails = new RiderDTO(null, "B", "a@b.c", null, 5.0, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);
        mvc.perform(put("/api/private/rider/"+String.valueOf(rider.getId())).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(newDetails))
                .header("Authorization", updatedtoken ))
                .andExpect(status().isOk());
    }

    @Test
    void whenUpdateNotExistentRider_thenReturnNotFound() throws Exception {
        RiderDTO newDetails = new RiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");
        newDetails.setLat(12.0);
        newDetails.setLng(93.0);

        String updatedtoken = "Bearer " + JWT.create()
            .withSubject( "0" )
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        mvc.perform(put("/api/private/rider/0").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(newDetails))
                .header("Authorization", updatedtoken ))
                .andExpect(status().isNotFound());
    }
}
