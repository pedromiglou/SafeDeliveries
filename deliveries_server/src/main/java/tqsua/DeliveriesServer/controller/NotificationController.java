package tqsua.DeliveriesServer.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import tqsua.DeliveriesServer.model.Notification;
import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.service.NotificationService;
import tqsua.DeliveriesServer.service.OrderService;

@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OrderService orderService;

    @GetMapping(path="/notifications")
    public ResponseEntity<Object> getNotificationByUserId(@RequestParam(name="id") long id) throws IOException, InterruptedException {
        Notification r = notificationService.getNotificationByRider(id);
        if (r==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Order order = orderService.getOrderById(r.getOrder_id());
        if (order==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        HashMap<String, Object> response = new HashMap<>();
        response.put("rider_id", r.getRider_id());
        response.put("order_id", r.getOrder_id());
        response.put("pick_up_lat", order.getPick_up_lat());
        response.put("pick_up_lng", order.getPick_up_lng());
        response.put("deliver_lat", order.getDeliver_lat());
        response.put("deliver_lng", order.getDeliver_lng());
        response.put("weight", order.getWeight());
        response.put("creation_date", order.getCreation_date());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}