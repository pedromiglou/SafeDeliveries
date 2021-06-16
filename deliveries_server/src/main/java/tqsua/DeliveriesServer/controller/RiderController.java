package tqsua.DeliveriesServer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import tqsua.DeliveriesServer.model.Notification;
import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.RiderDTO;
import tqsua.DeliveriesServer.service.NotificationService;
import tqsua.DeliveriesServer.service.OrderService;
import tqsua.DeliveriesServer.service.RiderService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class RiderController {
    @Autowired
    private RiderService riderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private NotificationService notificationService;

    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path="/private/riders")
    public ArrayList<Rider> getAllRiders() throws IOException, InterruptedException {
        return riderService.getAllRiders();
    }

    @GetMapping(path="/private/rider")
    public ResponseEntity<Object> getRiderById(Authentication authentication ,@RequestParam(name="id") long id) {
        String rider_id = authentication.getName();

        if (!rider_id.equals(String.valueOf(id))) {
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Rider r = riderService.getRiderById(id);
        if (r==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PutMapping(path="/private/rider/{id}")
    public ResponseEntity<Object> updateRider(Authentication authentication ,@PathVariable(value="id") Long id, @Valid @RequestBody RiderDTO rider) throws IOException, InterruptedException {
        
        String rider_id = authentication.getName();

        if (!rider_id.equals(String.valueOf(id))) {
            HashMap<String, String> response = new HashMap<>();
            response.put("message", "Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Rider r = riderService.updateRider(id, rider);
        if (r == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
        // When Rider turns Online, then search for pending orders
        if (rider.getStatus() != null && rider.getStatus().equals("Online")) {
            ArrayList<Order> orders = orderService.getPendingOrders();
            ArrayList<Order> refused_orders = orderService.getRefusedOrders(r.getId());
            orders.removeAll(refused_orders);
            if (orders.size() != 0) {
                Order final_order = getBestOrder(orders, r);
                Notification notification_for_rider = new Notification(r.getId(), final_order.getOrder_id());
                this.notificationService.save(notification_for_rider);
            }
        }
        
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    public Order getBestOrder(ArrayList<Order> orders, Rider r) {
        Order final_order = orders.remove(0);
        double pick_up_lat = final_order.getPick_up_lat();
        double pick_up_lng = final_order.getPick_up_lng();
        double x1 = r.getLat();
        double y1 = r.getLng();
        double min_distance = Math.sqrt((pick_up_lat-x1)*(pick_up_lat-x1) + (pick_up_lng-y1)*(pick_up_lng-y1));
        for (Order o: orders) {
            pick_up_lat = o.getPick_up_lat();
            pick_up_lng = o.getPick_up_lng();
            double distance = Math.sqrt((pick_up_lat-x1)*(pick_up_lat-x1) + (pick_up_lng-y1)*(pick_up_lng-y1));
            if (distance < min_distance) {
                final_order = o;
                min_distance = distance;
            }
        }
        return final_order;
    }
}