package tqsua.OrdersServer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import tqsua.OrdersServer.model.Order;
import tqsua.OrdersServer.model.OrderDTO;

import javax.validation.Valid;
import tqsua.OrdersServer.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path="/orders")
    public ArrayList<Order> getAllOrders() throws IOException, InterruptedException {
        return orderService.getAllOrders();
    }

    @PostMapping(path="/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderDTO o) throws IOException, InterruptedException {
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

}