package tqsua.DeliveriesServer.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.Vehicle;
import tqsua.DeliveriesServer.repository.VehicleRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    
    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private VehicleService service;

    @AfterEach
    void tearDown() {
        reset(repository);
    }

    @Test
    void whenGetAllVehicles_thenReturnCorrectResults() throws Exception {
        ArrayList<Vehicle> response = new ArrayList<>();
        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0);
        Vehicle v2 = new Vehicle("BMW", "M4", "Carro", 320.0);
        response.add(v1);
        response.add(v2);
        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllVehicles()).isEqualTo(response);
    }

    @Test
    void whenGetVehicleById_thenReturnTheVehicle() {
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0);

        when(repository.findById(response.getId())).thenReturn(response);
        assertThat(service.getVehicleById(response.getId())).isEqualTo(response);
    }

    @Test
    void whenCreateVehicle_thenSaveIt() {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, false);
        Vehicle vehicle = new Vehicle("Audi", "A5", "Carro", 365.0);
        vehicle.setRider(rider);

        service.saveVehicle(vehicle);
        Mockito.verify(repository, VerificationModeFactory.times(1)).save(vehicle);
    }

    @Test
    void whenCreateVehicleWithoutTheNeededParameters_thenDoNotSaveIt() {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, false);
        Vehicle vehicle = new Vehicle(null, "A5", "Carro", 365.0);
        vehicle.setRider(rider);

        assertThat(service.saveVehicle(vehicle)).isNull();
        Mockito.verify(repository, VerificationModeFactory.times(0)).save(vehicle);
    }

    @Test
    void whenUpdateVehicle_onlyUpdateNotNullParameters() {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, false);
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0);
        response.setRider(rider);
        when(repository.findById(response.getId())).thenReturn(response);

        //check if status is updated while other parameter remains the same
        Vehicle newDetails = new Vehicle("BMW", null, null, null);
        service.updateVehicle(response.getId(), newDetails);
        assertThat(response.getBrand()).isEqualTo("BMW");
        assertThat(response.getModel()).isEqualTo("A5");

        //check if all parameters are updated
        newDetails = new Vehicle("BMW", "M4", "Carro", 320.0);
        service.updateVehicle(response.getId(), newDetails);
        assertThat(response.getBrand()).isEqualTo("BMW");
        assertThat(response.getModel()).isEqualTo("M4");
        assertThat(response.getCategory()).isEqualTo("Carro");
        assertThat(response.getCapacity()).isEqualTo(320.0);
    }

    @Test
    void whenUpdateNotExistentVehicle_returnNull() {
        Vehicle newDetails = new Vehicle("BMW", "M4", "Carro", 320.0);
        assertThat(service.updateVehicle(-1, newDetails)).isNull();
    }

    @Test
    void whenDeleteVehicle_deleteTheVehicle() {
        Rider rider = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, false);
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0);
        response.setRider(rider);
        when(repository.findById(response.getId())).thenReturn(response);

        this.repository.deleteById(response.getId());
        Mockito.verify(repository, VerificationModeFactory.times(1)).deleteById(rider.getId());
    }

    @Test
    void whenDeleteNotExistentVehicle_ignoreRequest() {
        when(repository.findById(-1L)).thenReturn(null);

        this.repository.deleteById(-1L);
        Mockito.verify(repository, VerificationModeFactory.times(1)).deleteById(-1L);
    }
}