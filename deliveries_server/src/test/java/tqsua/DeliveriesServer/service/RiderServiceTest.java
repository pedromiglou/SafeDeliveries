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
import tqsua.DeliveriesServer.repository.RiderRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RiderServiceTest {
    
    @Mock
    private RiderRepository repository;

    @InjectMocks
    private RiderService service;

    @Test
    void whenGetAllRiders_thenReturnCorrectResults() throws Exception {
        ArrayList<Rider> response = new ArrayList<>();
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0);
        Rider rider2 = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9);
        response.add(rider1);
        response.add(rider2);

        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllRiders()).isEqualTo(response);
        reset(repository);
    }

}
