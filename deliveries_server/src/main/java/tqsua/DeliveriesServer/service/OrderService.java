package tqsua.DeliveriesServer.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void notificate(String url, long order_id) throws IOException, InterruptedException{
        Map<Object, Object> data = new HashMap<>();
        data.put("order_id", order_id);
        var objectMapper = new ObjectMapper();
        var requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(data);

        System.out.println(url + "/api/orders/notificate");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(url + "/api/orders/notificate"))
                .header("Content-type", "application/json")
                .build();

        System.out.println(request);
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}