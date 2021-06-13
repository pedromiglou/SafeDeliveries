package tqsua.OrdersServer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.model.Order;
import tqsua.OrdersServer.repository.ItemRepository;
import tqsua.OrdersServer.repository.OrderRepository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    private static final String DELIVERIES_URL = "http://localhost:8080/api/";

    public ArrayList<Order> getAllOrders() throws IOException, InterruptedException {
        return this.orderRepository.findAll();
    }

    public String deliveryRequest(Order order) throws IOException, InterruptedException {
        if (order.getDeliver_lat() == null || order.getDeliver_lng() == null || order.getPick_up_lat()==null || order.getPick_up_lng()==null ||
             order.getStatus()==null || order.getItems().size() == 0 || order.getUser_id()==0) return null;

        Map<Object, Object> data = new HashMap<>();
        data.put("pick_up_lat", order.getPick_up_lat());
        data.put("pick_up_lng", order.getPick_up_lng());
        data.put("deliver_lat", order.getDeliver_lat());
        data.put("deliver_lng", order.getDeliver_lng());
        data.put("weight", order.getItems().stream().mapToDouble(Item::getWeight).sum());
        data.put("app_name", "SafeDeliveries");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(data);
        
        HttpRequest request = HttpRequest.newBuilder()
            .POST(BodyPublishers.ofString(requestBody))
            .uri(URI.create(DELIVERIES_URL + "orders"))
            .header("Content-type", "application/json")
            .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        
        if (response.statusCode() != 201) {
            return null;
        }
        return json.getString("deliver_id");
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