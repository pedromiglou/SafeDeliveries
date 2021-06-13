package tqsua.DeliveriesServer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

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

import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.model.OrderDTO;
import tqsua.DeliveriesServer.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static final Map<String, String> APP_NAMES = Stream.of(new String[][] {
        { "SafeDeliveries", "http://localhost:8081" }, 
      }).collect(Collectors.toMap(data -> data[0], data -> data[1]));


    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path="/orders")
    public ArrayList<Order> getAllOrders() throws IOException, InterruptedException {
        return orderService.getAllOrders();
    }

    @PostMapping(path="/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderDTO o) {
        String message = "message";
        HashMap<String, String> response = new HashMap<>();
        if (o.getDeliver_lat() == null || o.getDeliver_lng() == null || o.getPick_up_lat() == null || o.getPick_up_lng() == null) {
            response.put(message, "Error. Invalid coords.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (o.getWeight()<=0) {
            response.put(message, "Error. Invalid Weight.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (!APP_NAMES.containsKey(o.getApp_name())) {
            response.put(message, "Error. Invalid App name.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Order o1 = new Order(0, o.getPick_up_lat(), o.getPick_up_lng(), o.getDeliver_lat(), o.getDeliver_lng(), o.getWeight(), o.getApp_name());
        
        Order order = orderService.saveOrder(o1);
        if (order == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        response.put("deliver_id", String.valueOf(order.getOrder_id()));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}