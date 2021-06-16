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

    String token = this.getToken("1");

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
        riderRepository.deleteAll();
    }
    
    @Test
    void whenGetAllRiders_thenReturnResult() throws Exception {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "Admin");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline", "User");
        riderRepository.save(rider1);
        riderRepository.save(rider2);

        token = getToken(String.valueOf(rider1.getId()));

        mvc.perform(get("/api/private/riders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
    }

    @Test
    void whenGetAllRidersWithoutPermission_thenReturnUnauthorized() throws Exception {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        riderRepository.save(rider1);

        token = getToken(String.valueOf(rider1.getId()));

        mvc.perform(get("/api/private/riders").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }

    @Test
    void whenGetRidersStatistics_thenReturnResult() throws Exception {
        // 1 Online
        Rider rider = createRider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online", "Admin");
        riderRepository.save(rider);

        token = getToken(String.valueOf(rider.getId()));

        // 2 Offline
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline", "User");
        riderRepository.save(rider1);
        riderRepository.save(rider2);

        // 1 Online
        Rider rider3 = createRider("Ricardo", "Cruz", "ricardo2@gmail.com", "password1234", 4.0, "Online", "User");
        riderRepository.save(rider3);

        // 1 Delivering
        Rider rider4 = createRider("Ricardo", "Cruz", "ricardo3@gmail.com", "password1234", 4.0, "Delivering", "User");
        riderRepository.save(rider4);

        mvc.perform(get("/api/private/riders/statistics").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_riders", is(5)))
                .andExpect(jsonPath("$.online_riders", is(2)))
                .andExpect(jsonPath("$.offline_riders", is(2)))
                .andExpect(jsonPath("$.delivering_riders", is(1)));
    }

    @Test
    void whenGetRidersStatisticsWithoutPermissions_thenReturnUnauthorized() throws Exception {
        Rider rider = createRider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online", "User");
        riderRepository.save(rider);

        token = getToken(String.valueOf(rider.getId()));

        mvc.perform(get("/api/private/riders/statistics").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }

    @Test
    void whenGetRiderById_thenReturnRider() throws Exception {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        riderRepository.save(rider1);

        token = getToken(String.valueOf(rider1.getId()));

        mvc.perform(get("/api/private/rider?id="+String.valueOf(rider1.getId())).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token )
                .content(JsonUtil.toJson(rider1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) rider1.getId())));

    }

    @Test
    void whenGetRiderByInvalidId_thenReturnRider() throws Exception {
        token = getToken("-1");

        mvc.perform(get("/api/private/rider?id=-1").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token ))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenUpdateRider_thenReturnOk() throws Exception {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        riderRepository.save(rider);
        RiderDTO newDetails = createRiderDTO("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");

        token = getToken(String.valueOf(rider.getId()));

        //with all arguments
        mvc.perform(put("/api/private/rider/"+String.valueOf(rider.getId())).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(newDetails))
                .header("Authorization", token ))
                .andExpect(status().isOk());

        //with less arguments
        newDetails = createRiderDTO(null, "B", "a@b.c", null, 5.0, "Offline");

        mvc.perform(put("/api/private/rider/"+String.valueOf(rider.getId())).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(newDetails))
                .header("Authorization", token ))
                .andExpect(status().isOk());
    }

    @Test
    void whenUpdateNotExistentRider_thenReturnNotFound() throws Exception {
        RiderDTO newDetails = createRiderDTO("A", "B", "a@b.c", "abcdefgh", 5.0, "Offline");

        token = getToken("0");

        mvc.perform(put("/api/private/rider/0").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(newDetails))
                .header("Authorization", token ))
                .andExpect(status().isNotFound());
    }

    public Rider createRider(String firstname, String lastname, String email, String password, double rating, String status, String account_type) {
        Rider rider = new Rider(firstname, lastname, email, password, rating, status);
        rider.setAccountType(account_type);
        rider.setLat(12.0);
        rider.setLng(93.0);
        return rider;
    }

    public RiderDTO createRiderDTO(String firstname, String lastname, String email, String password, Double rating, String status) {
        RiderDTO rider = new RiderDTO(firstname, lastname, email, password, rating, status);
        rider.setLat(12.0);
        rider.setLng(93.0);
        return rider;
    }

    public String getToken(String id) {
        String token = "Bearer " + JWT.create()
            .withSubject( id )
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
        return token;
    }
}
