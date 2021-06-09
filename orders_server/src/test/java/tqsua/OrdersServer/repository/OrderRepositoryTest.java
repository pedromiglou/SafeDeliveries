package tqsua.OrdersServer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqsua.OrdersServer.model.Order;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository repository;

    @Test
    void whenGetAllOrders_thenReturnCorrectResults() throws IOException, InterruptedException {
        Order order1 = new Order("Rua das Laranjeiras", "Rua Lourenço Peixinho", new Date(), "Entregue", 4);
        Order order2 = new Order("Rua da Universidade", "Bairro de Santiago", new Date(), "Entregue", 5);
        entityManager.persistAndFlush(order1);
        entityManager.persistAndFlush(order2);
        
        ArrayList<Order> found = repository.findAll();
        assertThat(found.get(0)).isEqualTo(order1);
        assertThat(found.get(1)).isEqualTo(order2);
    }


}