package tqsua.OrdersServer.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.net.http.HttpClient;

import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.model.Order;
import tqsua.OrdersServer.repository.ItemRepository;
import tqsua.OrdersServer.repository.OrderRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    
    @Mock
    private OrderRepository repository;

    @Mock
    private ItemRepository itemrepository;

    @InjectMocks
    private OrderService service;

    @Test
    void whenGetAllOrders_thenReturnCorrectResults() throws Exception {
        ArrayList<Order> response = new ArrayList<>();
        Order order1 = new Order(40.0, 30.0, 40.1, 31.1, "Entregue", 12);
        Order order2 = new Order(41.0, 31.0, 41.1, 32.1, "Entregue", 12);
        response.add(order1);
        response.add(order2);

        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllOrders()).isEqualTo(response);
        reset(repository);
    }

    @Test
    void whenSaveOrder_thenSaveOrder() throws Exception {
        Order order1 = new Order(40.0, 30.0, 40.1, 31.1, "PREPARING", 12);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        when(repository.save(order1)).thenReturn(order1);
        when(itemrepository.save(item1)).thenReturn(item1);
        when(itemrepository.save(item2)).thenReturn(item2);
        assertThat(service.saveOrder(order1)).isEqualTo(order1);
        reset(repository);
    }

    @Test
    void whenSaveOrderWithInvalidCoords_thenReturnNull() throws Exception {
        Order order1 = new Order(null, 30.0, 40.1, 31.1, "PREPARING", 12);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        when(repository.save(order1)).thenReturn(order1);
        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);

        order1 = new Order(40.1, null, 40.1, 31.1, "PREPARING", 12);

        when(repository.save(order1)).thenReturn(order1);
        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);

        order1 = new Order(40.1, 30.0, null, 31.1, "PREPARING", 12);

        when(repository.save(order1)).thenReturn(order1);
        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);

        order1 = new Order(40.1, 30.0, 40.1, null, "PREPARING", 12);

        when(repository.save(order1)).thenReturn(order1);
        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);
    }

    @Test
    void whenSaveOrderWithInvalidData_thenReturnNull() throws Exception {
        Order order1 = new Order(40.1, 30.0, 40.1, 31.1, null, 12);
        Set<Item> items = new HashSet<>();
        Item item1 = new Item("Casaco", "Roupa", 12.0);
        Item item2 = new Item("Telemovel", "Eletronica", 0.7);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        when(repository.save(order1)).thenReturn(order1);
        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);

        order1 = new Order(40.1, 30.0, 40.1, 31.1, "Preparing", 0);
        items.add(item1);
        items.add(item2);
        order1.setItems(items);

        when(repository.save(order1)).thenReturn(order1);
        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);
    }

    @Test
    void whenSaveOrderWithNoItems_thenReturnNull() throws Exception {
        Order order1 = new Order(40.1, 30.0, 40.1, 31.1, "PREPARING", 12);

        when(repository.save(order1)).thenReturn(order1);
        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);

        order1 = new Order(40.1, 30.0, 40.1, 31.1, "PREPARING", 12);
        Set<Item> items = new HashSet<>();
        order1.setItems(items);

        when(repository.save(order1)).thenReturn(order1);
        assertThat(service.saveOrder(order1)).isNull();
        reset(repository);

    }

    /* for teacher
    @Test
    void whendeliveryRequest_thenReturnCorrectResults() throws Exception {
        Order order = new Order(40.0, 30.0, 40.1, 31.1, "Entregue", 12);
        HttpResponse<String> response = new HttpResponse();
        when(HttpClient.newHttpClient().send(Mockito.any(), Mockito.any())).thenReturn(response);

        assertThat(service.deliveryRequest(order)).isEqualTo(null);
        reset(repository);
    }
    */
    
}