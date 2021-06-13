package tqsua.OrdersServer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.model.Order;
import tqsua.OrdersServer.repository.ItemRepository;
import tqsua.OrdersServer.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    public ArrayList<Order> getAllOrders() throws IOException, InterruptedException {
        return this.orderRepository.findAll();
    }

    public Order saveOrder(Order order) {
        if (order.getDeliver_lat() == null || order.getDeliver_lng() == null || order.getPick_up_lat()==null || order.getPick_up_lng()==null ||
             order.getStatus()==null || order.getItems().size() == 0 || order.getUser_id()==0) return null;
        Set<Item> order_items = order.getItems();
        this.orderRepository.save(order);
        for (Item e : order_items) {
            e.setOrder(order);
            this.itemRepository.save(e);
        }
        return order;
    }

}