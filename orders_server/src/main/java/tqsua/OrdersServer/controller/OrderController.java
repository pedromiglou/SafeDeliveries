package tqsua.OrdersServer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.coyote.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Object> createOrder(Authentication authentication ,@Valid @RequestBody OrderDTO o) throws IOException, InterruptedException {
        o.setStatus("PREPROCESSING");
        var message = "message";
        HashMap<String, String> response = new HashMap<>();
        if (o.getDeliver_lat() == null || o.getDeliver_lng() == null || o.getPick_up_lat() == null || o.getPick_up_lng() == null) {
            response.put(message, "Error. Invalid coords.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (o.getItems() == null || o.getItems().isEmpty()) {
            response.put(message, "Error. Order with 0 items.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (o.getUser_id()==0) {
            response.put(message, "Error. No user specified.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        String rider_id = authentication.getName();
        System.out.println(rider_id + " " + o.getUser_id());
        if (!rider_id.equals(String.valueOf(o.getUser_id()))) {
            response.put("message", "Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        var o1 = new Order(o.getPick_up_lat(), o.getPick_up_lng(), o.getDeliver_lat(), o.getDeliver_lng(), o.getStatus(), o.getUser_id());
        o1.setItems(o.getItems());
        
        // Get deliver id
        String deliverId = orderService.deliveryRequest(o1);
        if (deliverId == null) {
            response.put(message, "Error. Some error occured while connecting to Safe Deliveries.");
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
        var order = orderService.getOrderById(order_json.getLong("order_id"));
        var message = "message";
        HashMap<String, String> response = new HashMap<>();
        if (order == null){
            response.put(message, "Error.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        order.setStatus("DELIVERING");
        orderService.saveOrder(order);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Object> getOrderById(Authentication authentication, @PathVariable(value="id") Long id){
        String user_id = authentication.getName();
        var order_found = orderService.getOrderById(id);

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

}