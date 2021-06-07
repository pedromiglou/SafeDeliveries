package tqsua.DeliveriesServer.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

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
import tqsua.DeliveriesServer.repository.RiderRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RiderServiceTest {
    
    @Mock
    private RiderRepository repository;

    @InjectMocks
    private RiderService service;

    @AfterEach
    void tearDown() {
        reset(repository);
    }

    @Test
    void whenGetAllRiders_thenReturnCorrectResults() throws Exception {
        ArrayList<Rider> response = new ArrayList<>();
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        response.add(rider1);
        response.add(rider2);

        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllRiders()).isEqualTo(response);
        Mockito.verify(repository, VerificationModeFactory.times(1)).findAll();
    }

    @Test
    void whenGetRiderById_thenReturnRider() {
        Rider response = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");

        when(repository.findById(response.getId())).thenReturn(response);
        assertThat(service.getRiderById(response.getId())).isEqualTo(response);
        Mockito.verify(repository, VerificationModeFactory.times(1)).findById(response.getId());
    }

    @Test
    void whenGetRiderByInvalidId_thenReturnNull() {
        when(repository.findById(-1)).thenReturn(null);
        assertThat(service.getRiderById(-1)).isEqualTo(null);
        Mockito.verify(repository, VerificationModeFactory.times(1)).findById(-1);
    }

    @Test
    void whenUpdateRider_onlyUpdateNotNullParameters() {
        Rider response = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        when(repository.findById(response.getId())).thenReturn(response);

        //check if status is updated while other parameter remains the same
        service.updateRider(response.getId(), null, null, null, null, null, "Online");
        assertThat(response.getStatus()).isEqualTo("Online");
        assertThat(response.getFirstname()).isEqualTo("Ricardo");

        //check if all parameters are updated
        service.updateRider(response.getId(), "Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        assertThat(response.getFirstname()).isEqualTo("Diogo");
        assertThat(response.getLastname()).isEqualTo("Carvalho");
        assertThat(response.getEmail()).isEqualTo("diogo@gmail.com");
        assertThat(response.getPassword()).isEqualTo("password1234");
        assertThat(response.getRating()).isEqualTo(3.9);
        assertThat(response.getStatus()).isEqualTo("Offline");
    }


    @Test
    void whenSearchRiderExistsByEmail_ifRiderExists_ReturnTrue() {
        when(repository.existsRiderByEmail(anyString())).thenReturn(true);

        //check if service returns true when a rider with that email already exists
        service.existsRiderByEmail(anyString());
        assertThat(service.existsRiderByEmail(anyString())).isTrue();
    }

    @Test
    void whenSearchRiderExistsByEmail_ifRiderNotExists_ReturnFalse() {
        when(repository.existsRiderByEmail(anyString())).thenReturn(false);

        //check if service returns false when a rider with that email do not exists
        service.existsRiderByEmail(anyString());
        assertThat(service.existsRiderByEmail(anyString())).isFalse();
    }
}
