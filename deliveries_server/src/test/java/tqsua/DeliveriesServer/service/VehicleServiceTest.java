package tqsua.DeliveriesServer.service;

import static org.mockito.ArgumentMatchers.any;
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
import tqsua.DeliveriesServer.model.VehicleDTO;
import tqsua.DeliveriesServer.repository.VehicleRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    
    @Mock
    private VehicleRepository repository;

    @Mock
    private RiderService riderService;

    @InjectMocks
    private VehicleService service;

    @AfterEach
    void tearDown() {
        reset(repository);
        reset(riderService);
    }

    @Test
    void whenGetAllVehicles_thenReturnCorrectResults() throws Exception {
        ArrayList<Vehicle> response = new ArrayList<>();
        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "M4", "Carro", 320.0, "BBBBBB");
        response.add(v1);
        response.add(v2);
        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllVehicles()).isEqualTo(response);
    }

    @Test
    void whenGetVehicleById_thenReturnTheVehicle() {
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");

        when(repository.findById(response.getId())).thenReturn(response);
        assertThat(service.getVehicleById(response.getId())).isEqualTo(response);
    }

    @Test
    void whenGetVehiclesByRiderId_thenReturnVehicles() {
        ArrayList<Vehicle> response = new ArrayList<>();
        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        response.add(v1);
        response.add(v2);

        when(repository.findByRider(1L)).thenReturn(response);
        assertThat(service.getVehiclesByRiderId(1)).isEqualTo(response);
    }


    @Test
    void whenCreateVehicle_thenSaveIt() {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        when(riderService.getRiderById(0L)).thenReturn(rider);

        VehicleDTO vehicle = new VehicleDTO(null, "Audi", "A5", "Carro", 365.0, 0L, "AAAAAA");

        this.service.saveVehicle(vehicle);
        Mockito.verify(repository, VerificationModeFactory.times(1)).save(any());
    }

    @Test
    void whenCreateVehicleWithoutTheNeededParameters_thenDoNotSaveIt() {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        when(riderService.getRiderById(0L)).thenReturn(rider);

        VehicleDTO vehicle = new VehicleDTO(null, null, "A5", "Carro", 365.0, 0L, "BBBBBB");

        assertThat(service.saveVehicle(vehicle)).isNull();
        Mockito.verify(repository, VerificationModeFactory.times(0)).save(any());
    }

    @Test
    void whenUpdateVehicle_onlyUpdateNotNullParameters() {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        when(riderService.getRiderById(0L)).thenReturn(rider);
        Vehicle response = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");

        when(repository.findById(response.getId())).thenReturn(response);

        //check if status is updated while other parameter remains the same
        VehicleDTO newDetails = new VehicleDTO(null, null, "A5", "Carro", 365.0, 0L, "BBBBBB");
        service.updateVehicle(response.getId(), newDetails);
        assertThat(response.getBrand()).isEqualTo("Audi");
        assertThat(response.getModel()).isEqualTo("A5");

        //check if all parameters are updated
        newDetails = new VehicleDTO(null, "BMW", "M4", "Carro", 320.0, 0L, "CCCCCC");
        service.updateVehicle(response.getId(), newDetails);
        assertThat(response.getBrand()).isEqualTo("BMW");
        assertThat(response.getModel()).isEqualTo("M4");
        assertThat(response.getCategory()).isEqualTo("Carro");
        assertThat(response.getCapacity()).isEqualTo(320.0);
        assertThat(response.getRegistration()).isEqualTo("CCCCCC");
    }

    @Test
    void whenUpdateNotExistentVehicle_returnNull() {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline", "User");
        when(riderService.getRiderById(0L)).thenReturn(rider);

        VehicleDTO newDetails = new VehicleDTO(null, "BMW", "M4", "Carro", 320.0, 0L,"CCCCCC");
        assertThat(service.updateVehicle(-1, newDetails)).isNull();
    }

    @Test
    void whenDeleteVehicle_deleteTheVehicle() {
        when(repository.existsById(0L)).thenReturn(true);

        assertThat(this.service.deleteVehicle(0L)).isNotNull();
        Mockito.verify(repository, VerificationModeFactory.times(1)).deleteById(0L);
    }

    @Test
    void whenDeleteNotExistentVehicle_returnNull() {
        when(repository.existsById(-1L)).thenReturn(false);

        assertThat(this.service.deleteVehicle(-1L)).isNull();
        Mockito.verify(repository, VerificationModeFactory.times(0)).deleteById(-1L);
    }

    @Test
    void whenGetVehiclesByCapacity_returnResult() {
        ArrayList<Vehicle> response = new ArrayList<>();
        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 14.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "M4", "Carro", 75.0, "BBBBBB");
        Vehicle v3 = new Vehicle("BMW", "M4", "Carro", 101.0, "BBBBBB");
        response.add(v1);
        response.add(v2);
        response.add(v3);

        when(repository.findAll()).thenReturn(response);
        
        ArrayList<Integer> vehiclesCapacity = new ArrayList<>();
        vehiclesCapacity.add(0);
        vehiclesCapacity.add(1);
        vehiclesCapacity.add(0);
        vehiclesCapacity.add(1);
        vehiclesCapacity.add(1);
        
        assertThat(this.service.getVehiclesByCapacity()).isEqualTo(vehiclesCapacity);
        Mockito.verify(repository, VerificationModeFactory.times(1)).findAll();
    }

    public Rider createRider(String firstname, String lastname, String email, String password, double rating, String status, String account_type) {
        Rider rider = new Rider(firstname, lastname, email, password, rating, status);
        rider.setAccountType(account_type);
        rider.setLat(12.0);
        rider.setLng(93.0);
        return rider;
    }
}