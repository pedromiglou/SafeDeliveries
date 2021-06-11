package tqsua.OrdersServer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.model.Order;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository repository;

    @Test
    void whenGetAllItems_thenReturnCorrectResults() throws IOException, InterruptedException {
        Order order = new Order(40.0, 30.0, 40.1, 31.1, "Entregue", 12);
        entityManager.persistAndFlush(order);
        Item item1 = new Item("TV", "Informatica", 43.0);
        Item item2 = new Item("Frigorifico", "Eletrodomesticos", 290.6);

        item1.setOrder(order);
        item2.setOrder(order);
        entityManager.persistAndFlush(item1);
        entityManager.persistAndFlush(item2);
        
        ArrayList<Item> found = repository.findAll();
        assertThat(found.get(0)).isEqualTo(item1);
        assertThat(found.get(1)).isEqualTo(item2);
    }


}