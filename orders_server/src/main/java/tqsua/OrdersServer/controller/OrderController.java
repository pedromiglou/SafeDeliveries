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
    public ResponseEntity<Object> createOrder(@Valid @RequestBody Order o) {
        o.setStatus("PREPROCESSING");
        HashMap<String, String> response = new HashMap<>();
        if (o.getDeliver_lat() == null || o.getDeliver_lng() == null || o.getPick_up_lat() == null || o.getPick_up_lng() == null) {
            response.put("message", "Error. Invalid coords.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (o.getItems() == null || o.getItems().size() == 0) {
            response.put("message", "Error. Order with 0 items.");
            System.out.println("Estou ca dentro");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (o.getUser_id()==0) {
            response.put("message", "Error. No user specified.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Order order = orderService.saveOrder(o);
        if (order == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

}