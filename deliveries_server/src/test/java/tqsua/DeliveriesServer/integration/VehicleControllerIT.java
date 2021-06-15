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
import tqsua.DeliveriesServer.model.Vehicle;
import tqsua.DeliveriesServer.model.VehicleDTO;
import tqsua.DeliveriesServer.repository.RiderRepository;
import tqsua.DeliveriesServer.repository.VehicleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesServerApplication.class)
@AutoConfigureMockMvc
public class VehicleControllerIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private VehicleRepository repository;

    @Autowired
    private RiderRepository riderRepository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        riderRepository.deleteAll();
    }

    @Test
    void whenGetAllVehicles_thenReturnResult() throws Exception {
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        rider1.setLat(12.0);
        rider1.setLng(93.0);
        Rider rider2 = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        rider2.setLat(12.0);
        rider2.setLng(93.0);
        riderRepository.save(rider1);
        riderRepository.save(rider2);

        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "M4", "Carro", 320.0, "BBBBBB");
        v1.setRider(rider1);
        v2.setRider(rider2);
        repository.save(v1);
        repository.save(v2);

        mvc.perform(get("/api/vehicles").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
    }

    @Test
    void whenGetVehicleById_thenReturnVehicle() throws Exception {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        riderRepository.save(rider);
        Vehicle vehicle = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        vehicle.setRider(rider);
        repository.save(vehicle);

        mvc.perform(get("/api/vehicle?id="+String.valueOf(vehicle.getId())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) vehicle.getId())));
    }

    @Test
    void whenGetVehicleByInvalidId_thenReturnNotFound() throws Exception {
        mvc.perform(get("/api/vehicle?id=-1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void whenGetVehiclesByRiderId_thenReturnVehicles() throws Exception {
        Rider rider = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", 4.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        riderRepository.save(rider);
        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "M4", "Carro", 365.0, "BBBBBB");
        v1.setRider(rider);
        v2.setRider(rider);
        repository.save(v1);
        repository.save(v2);


        mvc.perform(get("/api/vehiclesbyrider?id="+rider.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }


    @Test
    void whenPostNewVehicle_thenCreateIt() throws Exception {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        riderRepository.save(rider);
        VehicleDTO vehicle = new VehicleDTO(null, "Audi", "A5", "Carro", 365.0, rider.getId(), "AAAAAA");
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        response.setRider(rider);


        mvc.perform(post("/api/vehicle").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(vehicle)))
                .andExpect(status().isCreated());
        assertThat(repository.findAll().size()).isEqualTo(1);
    }

    @Test
    void whenPostNewInvalidVehicle_thenReturnBadRequest() throws Exception {
        //missing rider
        VehicleDTO vehicle = new VehicleDTO(null, "Audi", "A5", "Carro", 365.0, null, "AAAAAA");

        mvc.perform(post("/api/vehicle").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(vehicle)))
                .andExpect(status().isBadRequest());
        assertThat(repository.findAll().size()).isEqualTo(0);
    }

    @Test
    void whenUpdateVehicle_thenUpdateIt() throws Exception {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        riderRepository.save(rider);

        Vehicle vehicle = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        vehicle.setRider(rider);
        repository.save(vehicle);

        VehicleDTO newDetails = new VehicleDTO(null, "BMW", null, null, null, null, null);

        mvc.perform(put("/api/vehicle/"+String.valueOf(vehicle.getId())).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        assertThat(repository.findById(vehicle.getId())).hasFieldOrPropertyWithValue("brand", "BMW");
    }

    @Test
    void whenUpdateNonExistentVehicle_thenReturnNotFound() throws Exception {
        VehicleDTO newDetails = new VehicleDTO(null, "BMW", null, null, null, null, null);

        mvc.perform(put("/api/vehicle/"+String.valueOf(-1)).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeletingVehicle_thenDeleteIt() throws Exception {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        rider.setLat(12.0);
        rider.setLng(93.0);
        riderRepository.save(rider);

        Vehicle vehicle = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAAA");
        vehicle.setRider(rider);
        repository.save(vehicle);

        mvc.perform(delete("/api/vehicle/"+String.valueOf(vehicle.getId()))).andExpect(status().isOk());
        assertThat(repository.existsById(vehicle.getId())).isFalse();
    }

    @Test
    void whenDeletingNotExistentVehicle_thenReturnNotFound() throws Exception {
        mvc.perform(delete("/api/vehicle/"+String.valueOf(0L))).andExpect(status().isNotFound());
    }
}
