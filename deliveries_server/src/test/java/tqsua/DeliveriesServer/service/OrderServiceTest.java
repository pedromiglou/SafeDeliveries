package tqsua.DeliveriesServer.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.repository.OrderRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    
    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderService service;

    @Test
    void whenGetAllOrders_thenReturnCorrectResults() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = new Order(0, 41.3, 32.4, 41.2, 32.3, 12.6, "SafeDeliveries");
        response.add(order1);
        response.add(order2);

        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllOrders()).isEqualTo(response);
        reset(repository);
    }


    @Test
    void whenGetPendingOrders_thenReturnCorrectResults() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = new Order(0, 41.3, 32.4, 41.2, 32.3, 12.6, "SafeDeliveries");
        response.add(order1);
        response.add(order2);

        when(repository.findPendingOrders()).thenReturn(response);
        assertThat(service.getPendingOrders()).isEqualTo(response);
        reset(repository);
    }

    @Test
    void whenGetRefusedOrders_thenReturnCorrectResults() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = new Order(0, 41.3, 32.4, 41.2, 32.3, 12.6, "SafeDeliveries");
        response.add(order1);
        response.add(order2);

        when(repository.findRefusedOrders(1)).thenReturn(response);
        assertThat(service.getRefusedOrders(1)).isEqualTo(response);
        reset(repository);
    }

    @Test
    void whenSaveOrder_thenSaveOrder() throws Exception {
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");

        when(repository.save(order1)).thenReturn(order1);
        assertThat(service.saveOrder(order1)).isEqualTo(order1);
        reset(repository);
    }
    
}