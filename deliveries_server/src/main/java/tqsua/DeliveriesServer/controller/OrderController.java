package tqsua.DeliveriesServer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import tqsua.DeliveriesServer.model.Notification;
import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.model.OrderDTO;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.service.NotificationService;
import tqsua.DeliveriesServer.service.OrderService;
import tqsua.DeliveriesServer.service.RiderService;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RiderService riderService;

    @Autowired
    private NotificationService notificationService;

    private static final String MESSAGE = "message";
    private static final String UNAUTHORIZED = "Unauthorized";

    private static final Map<String, String> APP_NAMES = Stream.of(new String[][] {
        { "SafeDeliveries", "http://localhost:8081" }, 
      }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path="/private/orders")
    public ResponseEntity<Object> getAllOrders(Authentication authentication) throws IOException, InterruptedException {
        HashMap<String, Object> response = new HashMap<>();
        Rider rider = riderService.getRiderById(Long.parseLong(authentication.getName()));
        if (!rider.getAccountType().equals("Admin")) {
            response.put(MESSAGE, UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping(path="/private/orders/statistics")
    public ResponseEntity<Object> getStatisticsOrders(Authentication authentication) throws IOException, InterruptedException {
        HashMap<String, Object> response = new HashMap<>();
        Rider rider = riderService.getRiderById(Long.parseLong(authentication.getName()));
        if (!rider.getAccountType().equals("Admin")) {
            response.put(MESSAGE, UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        
        int numOrders = orderService.getTotalOrders();
        int numPendingOrders = orderService.countOrders("Pending");
        int numDeliveringOrders = orderService.countOrders("Delivering");
        int numCompletedOrders = numOrders - numDeliveringOrders - numPendingOrders;
        ArrayList<Integer> ordersLast7Days = orderService.getOrdersLast7Days();
        ArrayList<Integer> ordersByWeight = orderService.getOrdersByWeight();

        response.put("total_orders", numOrders);
        response.put("pending_orders", numPendingOrders);
        response.put("delivering_orders", numDeliveringOrders);
        response.put("completed_orders", numCompletedOrders);
        response.put("orders_7_days", ordersLast7Days);
        response.put("orders_by_weight", ordersByWeight);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path="/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderDTO o) throws IOException, InterruptedException {
        HashMap<String, String> response = new HashMap<>();
        if (o.getDeliver_lat() == null || o.getDeliver_lng() == null || o.getPick_up_lat() == null || o.getPick_up_lng() == null) {
            response.put(MESSAGE, "Error. Invalid coords.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (o.getWeight()<=0) {
            response.put(MESSAGE, "Error. Invalid Weight.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (!APP_NAMES.containsKey(o.getApp_name())) {
            response.put(MESSAGE, "Error. Invalid App name.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Order o1 = new Order(0, o.getPick_up_lat(), o.getPick_up_lng(), o.getDeliver_lat(), o.getDeliver_lng(), o.getWeight(), o.getApp_name());
        o1.setStatus("Pending");
        Order order = orderService.saveOrder(o1);

        ArrayList<Rider> riders = riderService.getAvailableRiders(o.getWeight());
        if (riders.size() != 0) {
            Rider final_rider = getFinalRider(o1, riders);
            Notification notification_for_rider = new Notification(final_rider.getId(), o1.getOrder_id());
            this.notificationService.save(notification_for_rider);
        }
                
        if (order == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        response.put("deliver_id", String.valueOf(order.getOrder_id()));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping(path="/private/acceptorder")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> acceptOrder(Authentication authentication ,@RequestParam(name="order_id") long order_id, @RequestParam(name="rider_id") long rider_id) {
        String id = authentication.getName();

        if (!id.equals(String.valueOf(rider_id))) {
            HashMap<String, String> response = new HashMap<>();
            response.put(MESSAGE, UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        notificationService.delete(rider_id);
        Order order = orderService.updateRider(order_id, rider_id);
        riderService.changeStatus(rider_id, "Delivering");
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PostMapping(path="/private/declineorder")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> declineOrder(Authentication authentication ,@RequestParam(name="order_id") long order_id, @RequestParam(name="rider_id") long rider_id) throws IOException, InterruptedException {
        String id = authentication.getName();

        if (!id.equals(String.valueOf(rider_id))) {
            HashMap<String, String> response = new HashMap<>();
            response.put(MESSAGE, UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        
        notificationService.delete(rider_id);

        Order o = orderService.getOrderById(order_id);
        List<Long> refused_riders = o.getRefused_riders();
        refused_riders.add(rider_id);
        o.setRefused_riders(refused_riders);
        orderService.saveOrder(o);
        ArrayList<Rider> riders = riderService.getAvailableRiders(o.getWeight());
        for (Long idRider: refused_riders) {
            riders.removeIf(item -> item.getId() == idRider);
        }
        if (riders.size() != 0) {
            Rider final_rider = getFinalRider(o, riders);
            Notification notification_for_rider = new Notification(final_rider.getId(), o.getOrder_id());
            this.notificationService.save(notification_for_rider);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Rider getFinalRider(Order o, ArrayList<Rider> riders ) {
        double pick_up_lat = o.getPick_up_lat();
        double pick_up_lng = o.getPick_up_lng();
        Rider final_rider = riders.remove(0);
        double x1 = final_rider.getLat();
        double y1 = final_rider.getLng();
        double min_distance = Math.sqrt((pick_up_lat-x1)*(pick_up_lat-x1) + (pick_up_lng-y1)*(pick_up_lng-y1));
        for (Rider r : riders) {
            x1 = r.getLat();
            y1 = r.getLng();
            double distance = Math.sqrt((pick_up_lat-x1)*(pick_up_lat-x1) + (pick_up_lng-y1)*(pick_up_lng-y1));
            if (distance < min_distance) {
                final_rider = r;
                min_distance = distance;
            }
                
        }
        return final_rider;
    }
}