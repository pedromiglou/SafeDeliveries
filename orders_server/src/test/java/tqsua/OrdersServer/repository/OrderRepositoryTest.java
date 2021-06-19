package tqsua.OrdersServer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqsua.OrdersServer.model.Order;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository repository;

    @Test
    void whenGetAllOrders_thenReturnCorrectResults() throws IOException, InterruptedException {
        Order order1 = new Order(40.0, 30.0, 40.1, 31.1, "Entregue", 12);
        Order order2 = new Order(41.0, 31.0, 41.1, 32.1, "Entregue", 12);
        order1.setDeliver_id(1);
        order2.setDeliver_id(2);
        entityManager.persistAndFlush(order1);
        entityManager.persistAndFlush(order2);
        
        ArrayList<Order> found = repository.findAll();
        assertThat(found.get(0)).isEqualTo(order1);
        assertThat(found.get(1)).isEqualTo(order2);
    }

    @Test
    void whengetOrderByDeliverId_thenReturnCorrectResults() throws IOException, InterruptedException {
        Order order1 = new Order(40.0, 30.0, 40.1, 31.1, "Entregue", 12);
        Order order2 = new Order(41.0, 31.0, 41.1, 32.1, "Entregue", 12);
        order1.setDeliver_id(1);
        order2.setDeliver_id(2);
        entityManager.persistAndFlush(order1);
        entityManager.persistAndFlush(order2);
        
        Order found = repository.getOrderByDeliverId(1);
        assertThat(found).isEqualTo(order1);
    }

    @Test
    void whengetOrdersByUserId_thenReturnCorrectResults() throws IOException, InterruptedException {
        Order order1 = new Order(40.0, 30.0, 40.1, 31.1, "Entregue", 12);
        Order order2 = new Order(41.0, 31.0, 41.1, 32.1, "Entregue", 12);
        entityManager.persistAndFlush(order1);
        entityManager.persistAndFlush(order2);
        
        ArrayList<Order> response = new ArrayList<>();
        response.add(order1);
        response.add(order2);

        ArrayList<Order> found = repository.getOrdersByUserId(12);
        assertThat(found).isEqualTo(response);
    }


}