package tqsua.OrdersServer.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import tqsua.OrdersServer.model.Order;
import tqsua.OrdersServer.model.OrderDTO;

import javax.validation.Valid;
import tqsua.OrdersServer.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {
    private static final String MESSAGE = "message";
    private static final String UNAUTHORIZED = "Unauthorized";

    @Autowired
    private OrderService orderService;

    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path="/orders")
    public ArrayList<Order> getAllOrders() throws IOException, InterruptedException {
        return orderService.getAllOrders();
    }

    @PostMapping(path="/private/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createOrder(Authentication authentication ,@Valid @RequestBody OrderDTO o) throws IOException, InterruptedException, URISyntaxException {
        o.setStatus("PREPROCESSING");

        HashMap<String, String> response = new HashMap<>();
        if (o.getDeliver_lat() == null || o.getDeliver_lng() == null || o.getPick_up_lat() == null || o.getPick_up_lng() == null) {
            response.put(MESSAGE, "Error. Invalid coords.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (o.getItems() == null || o.getItems().isEmpty()) {
            response.put(MESSAGE, "Error. Order with 0 items.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (o.getUser_id()==0) {
            response.put(MESSAGE, "Error. No user specified.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        String rider_id = authentication.getName();
        if (!rider_id.equals(String.valueOf(o.getUser_id()))) {
            response.put(MESSAGE, UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        var o1 = new Order(o.getPick_up_lat(), o.getPick_up_lng(), o.getDeliver_lat(), o.getDeliver_lng(), o.getStatus(), o.getUser_id());
        o1.setItems(o.getItems());
        
        // Get deliver id
        String deliverId = orderService.deliveryRequest(o1, new RestTemplate());
        if (deliverId == null) {
            response.put(MESSAGE, "Error. Some error occured while connecting to Safe Deliveries.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // Set order with deliver id received
        o1.setDeliver_id(Long.parseLong(deliverId));

        // Save order
        var order = orderService.saveOrder(o1);

        if (order == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @PostMapping("/orders/notificate")
    public ResponseEntity<Object> notificate(@RequestBody String order_id){
        var order_json = new JSONObject(order_id);
        var order = orderService.getOrderByDeliverId(order_json.getLong("order_id"));
        HashMap<String, String> response = new HashMap<>();
        if (order == null){
            response.put(MESSAGE, "Error.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        order.setStatus("DELIVERING");
        orderService.saveOrder(order);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Object> getOrderById(Authentication authentication, @PathVariable(value="id") Long id){
        String user_id = authentication.getName();
        var order_found = orderService.getOrderByDeliverId(id);

        if (order_found == null) {
            HashMap<String, String> response = new HashMap<>();
            response.put(MESSAGE, "Not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!user_id.equals(String.valueOf(order_found.getUser_id()))) {
            HashMap<String, String> response = new HashMap<>();
            response.put(MESSAGE, UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(order_found, HttpStatus.OK);
    }

    @GetMapping("/private/user/{id}/orders")
    public ResponseEntity<Object> getOrdersByUserId(Authentication authentication, @PathVariable(value="id") Long id){
        String user_id = authentication.getName();
        if (!user_id.equals(String.valueOf(id))) {
            HashMap<String, String> response = new HashMap<>();
            response.put(MESSAGE, UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        ArrayList<Order> orders = orderService.getOrdersByUserId(id);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/private/order/confirm")
    public ResponseEntity<Object> confirmDelivery(Authentication authentication, @RequestBody String info) throws IOException, InterruptedException, URISyntaxException{
        String user_id = authentication.getName();
        var order_json = new JSONObject(info);
        var order = orderService.getOrderByDeliverId(order_json.getLong("order_id"));
        HashMap<String, String> response = new HashMap<>();
        if (order == null){
            response.put(MESSAGE, "Order not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (!user_id.equals(String.valueOf(order.getUser_id()))) {
            response.put(MESSAGE, UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        int rating = (int) order_json.getLong("rating");
        if (rating < 1 || rating > 5) {
            response.put(MESSAGE, "Invalid rating value.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        var res = orderService.confirm(order.getDeliver_id(), rating, new RestTemplate());
        if (res == null) {
            response.put(MESSAGE, "Some error occured while connecting to SafeDeliveries.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        order.setStatus("FINISHED");
        order.setRating(rating);
        return new ResponseEntity<>(orderService.saveOrder(order), HttpStatus.OK);
    }

}