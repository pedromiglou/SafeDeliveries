package tqsua.DeliveriesServer.repository;

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

    @Test
    void whenGetAllRiders_thenReturnCorrectResults() throws IOException, InterruptedException {
        Rider rider1 = new Rider("Ricardo", "Cruz", "ricardo@gmail.com", "password1234", 4.0);
        Rider rider2 = new Rider("Diogo", "Carvalho", "diogo@gmail.com", "password1234", 3.9);
        entityManager.persistAndFlush(rider1);
        entityManager.persistAndFlush(rider2);
        
        ArrayList<Rider> found = repository.findAll();
        assertThat(found.get(0)).isEqualTo(rider1);
        assertThat(found.get(1)).isEqualTo(rider2);
    }


}