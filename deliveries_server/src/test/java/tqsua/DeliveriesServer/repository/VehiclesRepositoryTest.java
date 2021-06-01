package tqsua.DeliveriesServer.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqsua.DeliveriesServer.model.Vehicles;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;

@DataJpaTest
class VehiclesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VehiclesRepository repository;

    @Test
    void whenGetAllVehicles_thenReturnCorrectResults() throws IOException, InterruptedException {
        Vehicles v1 = new Vehicles("Audi", "A5", "Carro", 365.0);
        Vehicles v2 = new Vehicles("BMW", "420", "Carro", 350.9);
        entityManager.persistAndFlush(v1);
        entityManager.persistAndFlush(v2);

        ArrayList<Vehicles> found = repository.findAll();
        assertThat(found.get(0)).isEqualTo(v1);
        assertThat(found.get(1)).isEqualTo(v2);
    }

    
}