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
import tqsua.DeliveriesServer.JsonUtil;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.Vehicle;
import tqsua.DeliveriesServer.model.VehicleDTO;
import tqsua.DeliveriesServer.service.RiderService;
import tqsua.DeliveriesServer.service.VehicleService;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @AfterEach
    void tearDown() {
        reset(service);
    }

    @Test
    void whenGetAllVehicles_thenReturnResult() throws Exception {
        ArrayList<Vehicle> response = new ArrayList<>();
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");

        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "M4", "Carro", 320.0, "BBBBBB");
        v1.setRider(rider1);
        v2.setRider(rider2);
        response.add(v1);
        response.add(v2);
        given(service.getAllVehicles()).willReturn(response);

        mvc.perform(get("/api/vehicles").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id").isNotEmpty());
        verify(service, VerificationModeFactory.times(1)).getAllVehicles();
    }

    @Test
    void whenGetVehicleById_thenReturnVehicle() throws Exception {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        response.setRider(rider);
        given(service.getVehicleById(response.getId())).willReturn(response);

        mvc.perform(get("/api/vehicle?id="+String.valueOf(response.getId())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) response.getId())));
        verify(service, VerificationModeFactory.times(1)).getVehicleById(response.getId());
    }

    @Test
    void whenGetVehicleByInvalidId_thenReturnNotFound() throws Exception {
        given(service.getVehicleById(-1L)).willReturn(null);

        mvc.perform(get("/api/vehicle?id=-1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).getVehicleById(-1);
    }

    @Test
    void whenGetVehiclesByRiderId_thenReturnVehicles() throws Exception {
        Rider rider = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "diogo123", 4.0, "Offline");
        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "M4", "Carro", 365.0, "AAAAAA");
        v1.setRider(rider);
        v2.setRider(rider);
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(v1);
        vehicles.add(v2);

        given(service.getVehiclesByRiderId(rider.getId())).willReturn(vehicles);

        mvc.perform(get("/api/vehiclesbyrider?id="+rider.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(service, VerificationModeFactory.times(1)).getVehiclesByRiderId(anyLong());
    }


    @Test
    void whenPostNewVehicle_thenCreateIt() throws Exception {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        VehicleDTO vehicle = new VehicleDTO(null, "Audi", "A5", "Carro", 365.0, 0L, "AAAAAA");
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        response.setRider(rider);

        given(service.saveVehicle(vehicle)).willReturn(response);
        given(riderService.saveRider(response.getRider())).willReturn(null);

        mvc.perform(post("/api/vehicle").contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.toJson(vehicle)))
                .andExpect(status().isCreated());
        verify(service, VerificationModeFactory.times(1)).saveVehicle(vehicle);
    }

    @Test
    void whenPostNewInvalidVehicle_thenReturnBadRequest() throws Exception {
        //missing rider
        VehicleDTO vehicle = new VehicleDTO(null, "Audi", "A5", "Carro", 365.0, null,"AAAAAAA");

        given(service.saveVehicle(vehicle)).willReturn(null);
        mvc.perform(post("/api/vehicle").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(vehicle)))
                .andExpect(status().isBadRequest());
        verify(service, VerificationModeFactory.times(1)).saveVehicle(vehicle);
    }

    @Test
    void whenUpdateVehicle_thenUpdateIt() throws Exception {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Vehicle vehicle = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        vehicle.setRider(rider);

        VehicleDTO newDetails = new VehicleDTO(null, "BMW", null, null, null, 0L, "AAAAAA");
        given(service.updateVehicle(vehicle.getId(), newDetails)).willReturn(vehicle);

        mvc.perform(put("/api/vehicle/"+String.valueOf(vehicle.getId())).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).updateVehicle(vehicle.getId(), newDetails);
    }

    @Test
    void whenUpdateNonExistentVehicle_thenReturnNotFound() throws Exception {
        VehicleDTO newDetails = new VehicleDTO(null, "BMW", null, null, null, 0L, "AAAAAA");
        given(service.updateVehicle(-1, newDetails)).willReturn(null);

        mvc.perform(put("/api/vehicle/"+String.valueOf(-1)).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newDetails)))
                .andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).updateVehicle(-1, newDetails);
    }

    
    @Test
    void whenDeletingVehicle_thenDeleteIt() throws Exception {
        given(service.deleteVehicle(0L)).willReturn(0L);

        mvc.perform(delete("/api/vehicle/"+String.valueOf(0L))).andExpect(status().isOk());
        verify(service, VerificationModeFactory.times(1)).deleteVehicle(0L);
    }

    @Test
    void whenDeletingNotExistentVehicle_thenReturnNotFound() throws Exception {
        given(service.deleteVehicle(0L)).willReturn(null);

        mvc.perform(delete("/api/vehicle/"+String.valueOf(0L))).andExpect(status().isNotFound());
        verify(service, VerificationModeFactory.times(1)).deleteVehicle(0L);
    }
}
