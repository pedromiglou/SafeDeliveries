package tqsua.DeliveriesServer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.Vehicle;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;

@DataJpaTest
class VehicleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehicleRepository repository;

    @Test
    void whenGetAllVehicles_thenReturnCorrectResults() throws IOException, InterruptedException {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");

        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "420", "Carro", 350.9, "BBBBBB");
        v1.setRider(rider1);
        v2.setRider(rider2);
        entityManager.persistAndFlush(rider1);
        entityManager.persistAndFlush(rider2);
        entityManager.persistAndFlush(v1);
        entityManager.persistAndFlush(v2);

        ArrayList<Vehicle> found = repository.findAll();
        assertThat(found.get(0)).isEqualTo(v1);
        assertThat(found.get(1)).isEqualTo(v2);
    }

    @Test
    void whenGetVehicleById_thenReturnVehicle() {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "420", "Carro", 350.9, "BBBBBB");
        v1.setRider(rider1);
        v2.setRider(rider2);
        entityManager.persistAndFlush(rider1);
        entityManager.persistAndFlush(rider2);
        entityManager.persistAndFlush(v1);
        entityManager.persistAndFlush(v2);

        Vehicle found = repository.findById(v1.getId());
        assertThat(found).isEqualTo(v1);
    }

    @Test
    void whenGetAllVehicleOfARiderId_thenReturnVehicles() {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        Vehicle v1 = new Vehicle("Audi", "A5", "Carro", 365.0, "AAAAAA");
        Vehicle v2 = new Vehicle("BMW", "420", "Carro", 350.9, "BBBBBB");
        v1.setRider(rider1);
        v2.setRider(rider2);
        entityManager.persistAndFlush(rider1);
        entityManager.persistAndFlush(rider2);
        entityManager.persistAndFlush(v1);
        entityManager.persistAndFlush(v2);

        ArrayList<Vehicle> found = repository.findByRider(rider1.getId());
        assertThat(found.get(0)).isEqualTo(v1);
    }

    public Rider createRider(String firstname, String lastname, String email, String password, double rating, String status) {
        Rider rider = new Rider(firstname, lastname, email, password, rating, status);
        rider.setAccountType("User");
        rider.setLat(12.0);
        rider.setLng(93.0);
        return rider;
    }

    
}