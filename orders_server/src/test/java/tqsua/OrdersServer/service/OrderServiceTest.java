package tqsua.OrdersServer.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tqsua.OrdersServer.model.Order;
import tqsua.OrdersServer.repository.OrderRepository;

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
        Order order1 = new Order("Rua das Laranjeiras", "Rua Lourenço Peixinho", new Date(), "Entregue", 4);
        Order order2 = new Order("Rua da Universidade", "Bairro de Santiago", new Date(), "Entregue", 5);
        response.add(order1);
        response.add(order2);

        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllOrders()).isEqualTo(response);
        reset(repository);
    }

}