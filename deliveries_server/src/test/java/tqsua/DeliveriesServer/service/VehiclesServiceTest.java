package tqsua.DeliveriesServer.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.Vehicles;
import tqsua.DeliveriesServer.repository.VehiclesRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VehiclesServiceTest {
    
    @Mock
    private VehiclesRepository repository;

    @InjectMocks
    private VehiclesService service;

    @Test
    void whenGetAllVehicles_thenReturnCorrectResults() throws Exception {
        ArrayList<Vehicles> response = new ArrayList<>();
        Vehicles v1 = new Vehicles("Audi", "A5", "Carro", 365.0);
        Vehicles v2 = new Vehicles("BMW", "M4", "Carro", 320.0);
        response.add(v1);
        response.add(v2);
        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllVehicles()).isEqualTo(response);
        reset(repository);
    }

}