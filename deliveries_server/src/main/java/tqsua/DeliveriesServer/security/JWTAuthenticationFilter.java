package tqsua.DeliveriesServer.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tqsua.DeliveriesServer.model.Notification;
import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.RiderDTO;
import tqsua.DeliveriesServer.service.NotificationService;
import tqsua.DeliveriesServer.service.OrderService;
import tqsua.DeliveriesServer.service.RiderService;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    @Autowired
    private RiderService riderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private NotificationService notificationService;


    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
        this.authenticationManager = authenticationManager;
        this.riderService= ctx.getBean(RiderService.class);
        this.orderService= ctx.getBean(OrderService.class);
        this.notificationService= ctx.getBean(NotificationService.class);
        setFilterProcessesUrl("/api/login"); 
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            Rider creds = new ObjectMapper()
                    .readValue(req.getInputStream(), Rider.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject(  String.valueOf(( (Rider) auth.getPrincipal() ).getId()) )
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        HashMap<String, String> map = new HashMap<>();
        
        Rider updatedRider = (Rider) auth.getPrincipal();
        RiderDTO r = new RiderDTO(null, null, null, null, null, "Online"); 
        Rider rider = riderService.updateRider(updatedRider.getId(), r);

        ArrayList<Order> orders;
        try {
            // Search for pending orders to assign the rider who just turned online
            orders = orderService.getPendingOrders();
            ArrayList<Order> refused_orders = orderService.getRefusedOrders(rider.getId());
            for (Order i : refused_orders) {
                orders.removeIf(item -> item.getOrder_id() == i.getOrder_id());
            }
            if (orders.size() != 0) {
                Order final_order = orders.remove(0);
                double pick_up_lat = final_order.getPick_up_lat();
                double pick_up_lng = final_order.getPick_up_lng();
                double x1 = rider.getLat();
                double y1 = rider.getLng();
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
                Notification notification_for_rider = new Notification(rider.getId(), final_order.getOrder_id());
                this.notificationService.save(notification_for_rider);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        map.put("id", String.valueOf(updatedRider.getId()));
        map.put("firstname", updatedRider.getFirstname());
        map.put("lastname", updatedRider.getLastname());
        map.put("email", updatedRider.getEmail());
        map.put("status", updatedRider.getStatus());
        map.put("rating", String.valueOf(updatedRider.getRating()));
        map.put("token", token);
        String json = new ObjectMapper().writeValueAsString(map);
        res.getWriter().write(json);
        res.getWriter().flush();
    }
}
