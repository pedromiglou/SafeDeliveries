package tqsua.DeliveriesServer.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
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
    void whenGetTotalOrders_thenReturnCorrectResults() throws Exception {
        when(repository.countAll()).thenReturn(2);
        assertThat(service.getTotalOrders()).isEqualTo(2);
        reset(repository);
    }

    @Test
    void whenGetOrdersLast7Days_thenReturnCorrectResults() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        order1.setCreation_date(LocalDateTime.now());
        Order order2 = new Order(0, 41.3, 32.4, 41.2, 32.3, 12.6, "SafeDeliveries");
        order2.setCreation_date(LocalDateTime.now().minusDays(1));
        Order order3 = new Order(0, 41.3, 32.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        order3.setCreation_date(LocalDateTime.now().minusDays(2));
        response.add(order1);
        response.add(order2);
        response.add(order3);

        ArrayList<Integer> orders7Days = new ArrayList<>();
        orders7Days.add(0);
        orders7Days.add(0);
        orders7Days.add(0);
        orders7Days.add(0);
        orders7Days.add(1);
        orders7Days.add(1);
        orders7Days.add(1);

        when(repository.findAll()).thenReturn(response);
        assertThat(service.getOrdersLast7Days()).isEqualTo(orders7Days);
        reset(repository);
    }

    @Test
    void whenGetOrdersByWeight_thenReturnCorrectResults() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 1.3, "SafeDeliveries");
        Order order2 = new Order(0, 41.3, 32.4, 41.2, 32.3, 15.6, "SafeDeliveries");
        Order order3 = new Order(0, 41.3, 32.4, 41.2, 31.3, 8.0, "SafeDeliveries");
        response.add(order1);
        response.add(order2);
        response.add(order3);

        ArrayList<Integer> ordersWeight = new ArrayList<>();
        ordersWeight.add(1);
        ordersWeight.add(1);
        ordersWeight.add(1);
        ordersWeight.add(0);

        when(repository.findAll()).thenReturn(response);
        assertThat(service.getOrdersByWeight()).isEqualTo(ordersWeight);
        reset(repository);
    }


    @Test
    void whenGetPendingOrders_thenReturnCorrectResults() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");
        Order order2 = new Order(0, 41.3, 32.4, 41.2, 32.3, 12.6, "SafeDeliveries");
        response.add(order1);
        response.add(order2);

        when(repository.findPendingOrders(10000)).thenReturn(response);
        assertThat(service.getPendingOrders(10000)).isEqualTo(response);
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
    void whenGetOrderById_thenReturnCorrectResults() throws Exception {
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");

        when(repository.findByPk(1)).thenReturn(order1);
        assertThat(service.getOrderById(1)).isEqualTo(order1);
        reset(repository);
    }

    @Test
    void whenSaveOrder_thenSaveOrder() throws Exception {
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");

        when(repository.save(order1)).thenReturn(order1);
        assertThat(service.saveOrder(order1)).isEqualTo(order1);
        reset(repository);
    }

    @Test
    void whenSaveOrderWithInvalidCoords_thenReturnNull() throws Exception {
        Order order1 = new Order(0, null, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");

        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);

        order1 = new Order(0, 30.4, null, 41.2, 31.3, 36.3, "SafeDeliveries");

        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);

        order1 = new Order(0, 30.4, 30.4, null, 31.3, 36.3, "SafeDeliveries");

        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);

        order1 = new Order(0, 42.9, 30.4, 41.2, null, 36.3, "SafeDeliveries");

        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);
    }


    @Test
    void whenSaveOrderWithInvalidAppName_thenReturnNull() throws Exception {
        Order order1 = new Order(0, 30.4, 30.4, 41.2, 31.3, 36.3, null);

        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);
    }

    @Test
    void whenSaveOrderWithInvalidWeight_thenReturnNull() throws Exception {
        Order order1 = new Order(0, 30.4, 30.4, 41.2, 31.3, -1.2, "SafeDeliveries");

        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);
    }

    @Test
    void whenUpdateRider_onlyUpdateNotNullParameters() {
        Order order1 = new Order(0, 40.3, 30.4, 41.2, 31.3, 36.3, "SafeDeliveries");

        when(repository.findByPk(1)).thenReturn(order1);
        order1.setRider_id(2);
        when(repository.save(order1)).thenReturn(order1);

        assertThat(service.updateRider(1, 2)).isEqualTo(order1);
        reset(repository);
    }
    
}