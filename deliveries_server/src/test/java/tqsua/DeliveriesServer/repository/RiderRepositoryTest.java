package tqsua.DeliveriesServer.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqsua.DeliveriesServer.model.Rider;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;

@DataJpaTest
class RiderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RiderRepository repository;

    @BeforeEach
    void setUp() {
        entityManager.clear();
        repository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        entityManager.clear();
    }

    @Test
    void whenGetAllRiders_thenReturnCorrectResults() throws IOException, InterruptedException {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        entityManager.persistAndFlush(rider1);
        entityManager.persistAndFlush(rider2);

        ArrayList<Rider> found = repository.findAll();
        assertThat(found.get(0)).isEqualTo(rider1);
        assertThat(found.get(1)).isEqualTo(rider2);
    }

    @Test
    void whenGetRiderById_thenReturnRider() {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        entityManager.persistAndFlush(rider1);
        entityManager.persistAndFlush(rider2);

        Rider found = repository.findById(rider1.getId());
        assertThat(found).isEqualTo(rider1);
    }

    @Test
    void whenGetRiderByInvalidId_thenReturnNull() {
        Rider rider1 = createRider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0, "Offline");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        entityManager.persistAndFlush(rider1);
        entityManager.persistAndFlush(rider2);

        Rider found = repository.findById(-1);
        assertThat(found).isNull();
    }

    @Test
    void whenSearchRiderExistsByEmail_ifRiderExists_thenReturnTrue() {
        Rider rider1 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        entityManager.persistAndFlush(rider1);

        assertThat(repository.existsRiderByEmail("diogo@gmail.com")).isTrue();
    }

    @Test
    void whenSearchRiderExistsByEmail_ifRiderNotExists_thenReturnFalse() {
        Rider rider1 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        entityManager.persistAndFlush(rider1);

        assertThat(repository.existsRiderByEmail("ricardo@gmail.com")).isFalse();
    }

    @Test
    void whenCountRidersByStatus_thenReturnExpectedResult() {
        Rider rider1 = createRider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9, "Offline");
        Rider rider2 = createRider("Diogo", "Carvalho", "diogo2@gmail.com", "password1234", 3.9, "Online");
        Rider rider3 = createRider("Diogo", "Carvalho", "diogo3@gmail.com", "password1234", 3.9, "Online");
        entityManager.persistAndFlush(rider1);
        entityManager.persistAndFlush(rider2);
        entityManager.persistAndFlush(rider3);

        assertThat(repository.countByState("Online")).isEqualTo(2);
    }

    public Rider createRider(String firstname, String lastname, String email, String password, double rating, String status) {
        Rider rider = new Rider(firstname, lastname, email, password, rating, status);
        rider.setAccountType("User");
        rider.setLat(12.0);
        rider.setLng(93.0);
        return rider;
    }

}