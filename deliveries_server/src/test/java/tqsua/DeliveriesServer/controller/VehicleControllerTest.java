package tqsua.DeliveriesServer.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqsua.DeliveriesServer.JsonUtil;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.Vehicle;
import tqsua.DeliveriesServer.model.VehicleDTO;
import tqsua.DeliveriesServer.security.SecurityConstants;
import tqsua.DeliveriesServer.service.RiderService;
import tqsua.DeliveriesServer.service.VehicleService;

import java.util.ArrayList;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(VehicleController.class)
@AutoConfigureMockMvc
@SpringBootTest
public class VehicleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VehicleService service;

    @MockBean
    private RiderService riderService;

    String token = this.getToken("1");

    String invalidtoken = this.getToken("5");

    @AfterEach
    void tearDown() {
        reset(service);
    }

    @Test
    void whenGetAllVehicles_thenReturnResult() throws Exception {
        ArrayList<Vehicle> response = new ArrayList<>();
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "Admin");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline", "User");

        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "M4", "Carro", 320.0, "BBBBBB");
        v1.setRider(rider1);
        v2.setRider(rider2);
        response.add(v1);
        response.add(v2);
        given(riderService.getRiderById(1)).willReturn(rider1);
        given(service.getAllVehicles()).willReturn(response);

        mvc.perform(get("/api/private/vehicles").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).getAllVehicles();
        verify(riderService, VerificationModeFactory.times(1)).getRiderById(1);
    }

    @Test
    void whenGetAllVehiclesWithoutPermission_thenReturnUnauthorized() throws Exception {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");

        given(riderService.getRiderById(1)).willReturn(rider1);

        mvc.perform(get("/api/private/vehicles").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(riderService, VerificationModeFactory.times(1)).getRiderById(1);
        verify(service, VerificationModeFactory.times(0)).getAllVehicles();
    }


    @Test
    void whenGetVehiclesStatistics_thenReturnResult() throws Exception {
        Rider rider = createRider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online", "Admin");

        ArrayList<Integer> response = new ArrayList<>();
        response.add(1);
        response.add(2);
        response.add(3);
        response.add(4);

        given(riderService.getRiderById(1)).willReturn(rider);
        given(service.getVehiclesByCapacity()).willReturn(response);

        mvc.perform(get("/api/private/vehicles/statistics").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicles_by_capacity", is(response)));
        verify(service, VerificationModeFactory.times(1)).getVehiclesByCapacity(); 
        verify(riderService, VerificationModeFactory.times(1)).getRiderById(1);        
    }

    @Test
    void whenGetVehiclesStatisticsWithoutPermissions_thenReturnUnauthorized() throws Exception {
        Rider rider = createRider("Rafael", "Baptista", "rafael@ua.pt", "1234", 5.0, "Online", "User");

        given(riderService.getRiderById(1)).willReturn(rider);

        mvc.perform(get("/api/private/vehicles/statistics").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
    }

    @Test
    void whenGetVehicleById_thenReturnVehicle() throws Exception {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        rider.setId(1);
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        response.setId(2);
        response.setRider(rider);
        given(service.getVehicleById(2)).willReturn(response);

        mvc.perform(get("/api/private/vehicle?id="+2).contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)));
        verify(service, VerificationModeFactory.times(1)).getVehicleById(2);
    }

    @Test
    void whenGetVehicleByIdWithInvalidToken_thenReturnUnauthorized() throws Exception {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        rider.setId(1);
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        response.setId(2);
        response.setRider(rider);
        given(service.getVehicleById(2)).willReturn(response);

        mvc.perform(get("/api/private/vehicle?id="+2).contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(1)).getVehicleById(2);
    }

    @Test
    void whenGetVehicleByInvalidId_thenReturnNotFound() throws Exception {
        given(service.getVehicleById(-1L)).willReturn(null);

        mvc.perform(get("/api/private/vehicle?id=-1").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).getVehicleById(-1);
    }

    @Test
    void whenGetVehiclesByRiderId_thenReturnVehicles() throws Exception {
        Rider rider = createRider("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", 4.0, "Offline", "User");
        rider.setId(1);
        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "M4", "Carro", 365.0, "AAAAAA");
        v1.setRider(rider);
        v2.setRider(rider);
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(v1);
        vehicles.add(v2);

        given(service.getVehiclesByRiderId(1)).willReturn(vehicles);

        mvc.perform(get("/api/private/rider/1/vehicles").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(service, VerificationModeFactory.times(1)).getVehiclesByRiderId(1);
    }

    @Test
    void whenGetVehiclesByRiderIdWithDifferentToken_thenReturnUnauthorized() throws Exception {
        Rider rider = createRider("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", 4.0, "Offline", "User");
        rider.setId(1);
        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "M4", "Carro", 365.0, "AAAAAA");
        v1.setRider(rider);
        v2.setRider(rider);
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(v1);
        vehicles.add(v2);

        given(service.getVehiclesByRiderId(1)).willReturn(vehicles);

        mvc.perform(get("/api/private/rider/1/vehicles").contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken ))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).getVehiclesByRiderId(1);
    }

    @Test
    void whenPostNewVehicle_thenCreateIt() throws Exception {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        rider.setId(1);
        VehicleDTO vehicle = new VehicleDTO(null, "Audi", "A5", "Carro", 365.0, 0L, "AAAAAA");
        vehicle.setRider(1L);
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        response.setRider(rider);

        given(service.saveVehicle(vehicle)).willReturn(response);
        given(riderService.saveRider(rider)).willReturn(null);

        mvc.perform(post("/api/private/vehicle").contentType(MediaType.APPLICATION_JSON).header("Authorization", token )
            .content(JsonUtil.toJson(vehicle)))
                .andExpect(status().isCreated());
        verify(service, VerificationModeFactory.times(1)).saveVehicle(vehicle);
    }

    @Test
    void whenPostNewVehicleWithDifferentToken_thenReturnUnauthorized() throws Exception {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        rider.setId(1);
        VehicleDTO vehicle = new VehicleDTO(null, "Audi", "A5", "Carro", 365.0, 0L, "AAAAAA");
        vehicle.setRider(1L);
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        response.setRider(rider);

        given(service.saveVehicle(vehicle)).willReturn(response);
        given(riderService.saveRider(rider)).willReturn(null);

        mvc.perform(post("/api/private/vehicle").contentType(MediaType.APPLICATION_JSON).header("Authorization", invalidtoken )
            .content(JsonUtil.toJson(vehicle)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).saveVehicle(vehicle);
    }

    @Test
    void whenPostNewInvalidVehicle_thenReturnBadRequest() throws Exception {
        //missing rider
        VehicleDTO vehicle = new VehicleDTO(null, "Audi", "A5", "Carro", 365.0, null,"AAAAAAA");
        vehicle.setRider(1L);

        given(service.saveVehicle(vehicle)).willReturn(null);
        mvc.perform(post("/api/private/vehicle").contentType(MediaType.APPLICATION_JSON).header("Authorization", token ).content(JsonUtil.toJson(vehicle)))
                .andExpect(status().isBadRequest());
        verify(service, VerificationModeFactory.times(1)).saveVehicle(vehicle);
    }

    @Test
    void whenUpdateVehicle_thenUpdateIt() throws Exception {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        rider.setId(1);
        Vehicle vehicle = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        vehicle.setRider(rider);

        VehicleDTO newDetails = new VehicleDTO(null, "BMW", null, null, null, 0L, "AAAAAA");
        newDetails.setRider(1L);
        given(service.getVehicleById(vehicle.getId())).willReturn(vehicle);
        given(service.updateVehicle(vehicle.getId(), newDetails)).willReturn(vehicle);

        mvc.perform(put("/api/private/vehicle/"+String.valueOf(vehicle.getId())).header("Authorization", token ).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateVehicle(vehicle.getId(), newDetails);
    }

    @Test
    void whenUpdateVehicleWithDifferentToken_thenReturnUnauthorized() throws Exception {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        rider.setId(1);
        Vehicle vehicle = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        vehicle.setRider(rider);

        VehicleDTO newDetails = new VehicleDTO(null, "BMW", null, null, null, 0L, "AAAAAA");
        newDetails.setRider(1L);
        given(service.getVehicleById(vehicle.getId())).willReturn(vehicle);
        given(service.updateVehicle(vehicle.getId(), newDetails)).willReturn(vehicle);

        mvc.perform(put("/api/private/vehicle/"+String.valueOf(vehicle.getId())).header("Authorization", invalidtoken ).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).updateVehicle(vehicle.getId(), newDetails);
    }

    @Test
    void whenUpdateNonExistentVehicle_thenReturnNotFound() throws Exception {
        VehicleDTO newDetails = new VehicleDTO(null, "BMW", null, null, null, 0L, "AAAAAA");
        newDetails.setRider(1L);
        given(service.updateVehicle(-1, newDetails)).willReturn(null);

        mvc.perform(put("/api/private/vehicle/"+String.valueOf(-1)).contentType(MediaType.APPLICATION_JSON).header("Authorization", token ).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(0)).updateVehicle(-1, newDetails);
        verify(service, VerificationModeFactory.times(1)).getVehicleById(-1);
    }

    
    @Test
    void whenDeletingVehicle_thenDeleteIt() throws Exception {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        rider.setId(1);
        Vehicle vehicle = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        vehicle.setRider(rider);

        given(service.getVehicleById(2L)).willReturn(vehicle);
        given(service.deleteVehicle(2L)).willReturn(0L);

        mvc.perform(delete("/api/private/vehicle/2").header("Authorization", token )).andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).deleteVehicle(2L);
    }

    @Test
    void whenDeletingVehicleWithDifferentToken_thenReturnUnauthorized() throws Exception {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        rider.setId(1);
        Vehicle vehicle = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        vehicle.setRider(rider);

        given(service.getVehicleById(2L)).willReturn(vehicle);
        given(service.deleteVehicle(2L)).willReturn(0L);

        mvc.perform(delete("/api/private/vehicle/2").header("Authorization", invalidtoken ))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message", is("Unauthorized")));
        verify(service, VerificationModeFactory.times(0)).deleteVehicle(2L);
        verify(service, VerificationModeFactory.times(1)).getVehicleById(2L);
    }

    @Test
    void whenDeletingNotExistentVehicle_thenReturnNotFound() throws Exception {
        given(service.getVehicleById(0L)).willReturn(null);

        mvc.perform(delete("/api/private/vehicle/"+String.valueOf(0L)).header("Authorization", token )).andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).getVehicleById(0L);
    }

    public String getToken(String id) {
        String token = "Bearer " + JWT.create()
            .withSubject( id )
            .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
        return token;
    }

    public Rider createRider(String firstname, String lastname, String email, String password, double rating, String status, String account_type) {
        Rider rider = new Rider(firstname, lastname, email, password, rating, status);
        rider.setAccountType(account_type);
        rider.setLat(12.0);
        rider.setLng(93.0);
        return rider;
    }
}
