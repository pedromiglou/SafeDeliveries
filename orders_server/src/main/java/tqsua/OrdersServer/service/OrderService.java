package tqsua.OrdersServer.service;

import java.io.IOException;
import java.util.*;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.model.Order;
import tqsua.OrdersServer.repository.ItemRepository;
import tqsua.OrdersServer.repository.OrderRepository;

import java.net.URI;
import java.net.URISyntaxException;


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

    public String deliveryRequest(Order order, RestTemplate restClient) throws IOException, InterruptedException, URISyntaxException {
        Map<Object, Object> data = new HashMap<>();
        data.put("pick_up_lat", order.getPick_up_lat());
        data.put("pick_up_lng", order.getPick_up_lng());
        data.put("deliver_lat", order.getDeliver_lat());
        data.put("deliver_lng", order.getDeliver_lng());
        data.put("weight", order.getItems().stream().mapToDouble(Item::getWeight).sum());
        data.put("app_name", "SafeDeliveries");

        final String baseUrl = DELIVERIES_URL + "orders";
        URI uri = new URI(baseUrl);
        
        ResponseEntity<String> result = restClient.postForEntity(uri, data, String.class);
        var json = new JSONObject(result.getBody());
        
        if (result.getStatusCode().value() != 201) {
            return null;
        }
        return json.getString("deliver_id");
    }

    public Order saveOrder(Order order) {
        if (order.getDeliver_lat() == null || order.getDeliver_lng() == null || order.getPick_up_lat()==null || order.getPick_up_lng()==null ||
             order.getStatus()==null || (order.getItems() != null && order.getItems().isEmpty()) || order.getItems() == null || order.getUser_id()==0) return null;
        
        Set<Item> orderItems = order.getItems();
        this.orderRepository.save(order);
        for (Item e : orderItems) {
            e.setOrder(order);
            this.itemRepository.save(e);
        }
        return order;
    }

    public Order getOrderById(long order_id){
        Order order = orderRepository.getOrderByDeliverId(order_id);
        return order;
    }
}