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

}