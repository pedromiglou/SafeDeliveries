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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.RiderDTO;
import tqsua.DeliveriesServer.repository.RiderRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RiderServiceTest {
    
    @Mock
    private RiderRepository repository;

    @InjectMocks
    private RiderService service;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();


    @AfterEach
    void tearDown() {
        reset(repository);
    }

    @Test
    void whenGetAllRiders_thenReturnCorrectResults() throws Exception {
        ArrayList<Rider> response = new ArrayList<>();
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        response.add(rider1);
        response.add(rider2);

        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllRiders()).isEqualTo(response);
        Mockito.verify(repository, VerificationModeFactory.times(1)).findAll();
    }

    @Test
    void whenGetAvailableRiders_thenReturnCorrectResults() throws Exception {
        ArrayList<Rider> response = new ArrayList<>();
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Online");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Online");
        Rider rider3 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Online");
        response.add(rider1);
        response.add(rider2);
        response.add(rider3);

        when(repository.findAvailableRiders(23.0)).thenReturn(response);
        assertThat(service.getAvailableRiders(23.0)).isEqualTo(response);
        Mockito.verify(repository, VerificationModeFactory.times(1)).findAvailableRiders(23.0);
    }

    @Test
    void whenGetRiderById_thenReturnRider() {
        Rider response = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");

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
        Rider response = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        when(repository.findById(response.getId())).thenReturn(response);

        RiderDTO newDetails = createRiderDTO(null, null, null, null, null, "Online");
        //check if status is updated while other parameter remains the same
        service.updateRider(response.getId(), newDetails);
        assertThat(response.getStatus()).isEqualTo("Online");
        assertThat(response.getFirstname()).isEqualTo("Ricardo");

        //check if all parameters are updated
        newDetails = createRiderDTO("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        service.updateRider(response.getId(), newDetails);
        assertThat(response.getFirstname()).isEqualTo("Diogo");
        assertThat(response.getLastname()).isEqualTo("Carvalho");
        assertThat(response.getEmail()).isEqualTo("diogo@gmail.com");
        assertThat(response.getPassword()).isEqualTo("password1234");
        assertThat(response.getRating()).isEqualTo(3.9);
        assertThat(response.getStatus()).isEqualTo("Offline");
    }

    @Test
    void whenUpdateNotExistentRider_returnNull() {
        when(repository.findById(-1)).thenReturn(null);
        RiderDTO newDetails = createRiderDTO("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        assertThat(service.updateRider(-1, newDetails)).isNull();
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

    @Test
    void whenChangeStatus_ifRiderNotExists_ReturnNull() {
        when(repository.findById(1)).thenReturn(null);

        assertThat(service.changeStatus(1, "Online")).isNull();
    }

    @Test
    void whenChangeStatus_ifRiderExists_ReturnRider() {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        when(repository.findById(1)).thenReturn(rider);
        rider.setStatus("Online");
        when(repository.save(rider)).thenReturn(rider);

        assertThat(service.changeStatus(1, "Online")).isEqualTo(rider);
    }

    @Test
    void whenSaveRider_ReturnRider() {
        Rider rider = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = rider;
        rider2.setPassword(bCryptPasswordEncoder.encode(rider.getPassword()));
        when(repository.save(rider)).thenReturn(rider2);

        assertThat(service.saveRider(rider)).isEqualTo(rider2);
    }

    @Test
    void whenGetRidersByState_ReturnRider() {
        when(repository.countByState("Online")).thenReturn(2);

        assertThat(service.getRidersByState("Online")).isEqualTo(2);
    }

    @Test
    void whenGetRidersByInvalidState_ReturnRider() {
        assertThat(service.getRidersByState("invalidState")).isEqualTo(0);
    }

    public Rider createRider(String firstname, String lastname, String email, String password, double rating, String status) {
        Rider rider = new Rider(firstname, lastname, email, password, rating, status);
        rider.setAccountType("User");
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
}
