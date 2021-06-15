package tqsua.DeliveriesServer.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public ArrayList<Order> getAllOrders() throws IOException, InterruptedException {
        return this.orderRepository.findAll();
    }

    public ArrayList<Order> getPendingOrders() throws IOException, InterruptedException {
        return this.orderRepository.findPendingOrders();
    }

    public ArrayList<Order> getRefusedOrders(long id) throws IOException, InterruptedException {
        return this.orderRepository.findRefusedOrders(id);
    }

    public Order getOrderById(long id) throws IOException, InterruptedException {
        return this.orderRepository.findByPk(id);
    }

    public Order saveOrder(Order order) {
        if (order.getDeliver_lat() == null || order.getDeliver_lng() == null || order.getPick_up_lat()==null || order.getPick_up_lng()==null ||
            order.getApp_name() == null || order.getWeight() <= 0) return null;
        order = this.orderRepository.save(order);
        return order;
    }

    public Order updateRider(long order_id, long rider_id) {
        Order order = this.orderRepository.findByPk(order_id);
        order.setRider_id(rider_id);
        return this.orderRepository.save(order);
    }
}