package tqsua.DeliveriesServer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqsua.DeliveriesServer.model.Notification;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NotificationRepository repository;

    @Test
    void whenGetOrderByRider_thenReturnCorrectResults() throws IOException, InterruptedException {
        Notification notification1 = new Notification(1, 2);
        Notification notification2 = new Notification(2, 3);
        entityManager.persistAndFlush(notification1);
        entityManager.persistAndFlush(notification2);
        
        Notification found = repository.getByRiderId(1);
        assertThat(found).isEqualTo(notification1);
        Notification found2 = repository.getByRiderId(2);
        assertThat(found2).isEqualTo(notification2);
    }


}