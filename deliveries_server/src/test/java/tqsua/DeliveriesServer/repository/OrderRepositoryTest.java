package tqsua.DeliveriesServer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqsua.DeliveriesServer.model.Notification;
import tqsua.DeliveriesServer.model.Order;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository repository;

    @Test
    void whenGetAllOrders_thenReturnCorrectResults() throws IOException, InterruptedException {
        Order order1 = createOrder(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = createOrder(0, 41.3, 31.4, 43.2, 33.3, 12.3, "SafeDeliveries");
        entityManager.persistAndFlush(order1);
        entityManager.persistAndFlush(order2);
        
        ArrayList<Order> found = repository.findAll();
        assertThat(found.get(0)).isEqualTo(order1);
        assertThat(found.get(1)).isEqualTo(order2);
    }

    @Test
    void whenGetPendingOrders_thenReturnCorrectResults() throws IOException, InterruptedException {
        Order order1 = createOrder(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = createOrder(0, 41.3, 31.4, 43.2, 33.3, 12.3, "SafeDeliveries");
        
        order1 = entityManager.persistAndFlush(order1);
        Notification notification1 = new Notification(1, order1.getOrder_id());
        entityManager.persistAndFlush(order2);
        entityManager.persistAndFlush(notification1);
        
        ArrayList<Order> found = repository.findPendingOrders(10000);
        assertThat(found.get(0)).isEqualTo(order2);
    }

    @Test
    void whenGetRefusedOrders_thenReturnCorrectResults() throws IOException, InterruptedException {
        Order order1 = createOrder(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        List<Long> refused_riders = new ArrayList<>();
        refused_riders.add(1L);
        order1.setRefused_riders(refused_riders);
        
        entityManager.persistAndFlush(order1);
        
        ArrayList<Order> found = repository.findRefusedOrders(1);
        assertThat(found.get(0)).isEqualTo(order1);
    }

    @Test
    void whenCountAllOrders_thenReturnCorrectResults() throws IOException, InterruptedException {
        Order order1 = createOrder(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = createOrder(0, 41.3, 31.4, 43.2, 33.3, 12.3, "SafeDeliveries");
        entityManager.persistAndFlush(order1);
        entityManager.persistAndFlush(order2);
        
        int found = repository.countAll();
        assertThat(found).isEqualTo(2);
    }

    public Order createOrder(int rider_id, Double pick_up_lat, Double pick_up_lng, Double deliver_lat, Double deliver_lng, Double weight, String app_name ) {
        Order order1 = new Order(rider_id, pick_up_lat, pick_up_lng, deliver_lat, deliver_lng, weight, app_name);
        order1.setStatus("Pending");
        return order1;
    }
}