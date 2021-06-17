package tqsua.DeliveriesServer.service;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqsua.DeliveriesServer.model.Order;
import tqsua.DeliveriesServer.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public ArrayList<Order> getAllOrders() throws IOException, InterruptedException {
        return this.orderRepository.findAll();
    }

    public int getTotalOrders() throws IOException, InterruptedException {
        return this.orderRepository.countAll();
    }

    public ArrayList<Integer> getOrdersLast7Days() throws IOException, InterruptedException {
        ArrayList<Integer> ordersLast7Days = new ArrayList<Integer>();
        Map<Date, Date> dicionarioDias = new TreeMap<Date, Date>();

        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal2.set(Calendar.HOUR_OF_DAY, 23);
        cal2.set(Calendar.MINUTE, 59);
        cal2.set(Calendar.SECOND, 59);

        Date date = cal.getTime();
        
        Date date_fim = cal2.getTime();
        dicionarioDias.put(date, date_fim);
        for (int i = 1; i < 7; i++) {
            cal.add(Calendar.DATE, -1);
            cal2.add(Calendar.DATE, -1);
            date = cal.getTime();
            date_fim = cal2.getTime();
            dicionarioDias.put(date, date_fim);
        }
        ArrayList<Order> orders = orderRepository.findAll();

        List<Order> ordersVerified = new ArrayList<>();
        
        
        for (Map.Entry<Date,Date> entry : dicionarioDias.entrySet()) {
            Date dia = entry.getKey();
            Date data_fim_dia = entry.getValue();
            
            int contador = 0;
            for (Order o : orders) {
                Date dateFromLocalDT = Date.from(o.getCreation_date().atZone(ZoneId.systemDefault()).toInstant());
                if (dateFromLocalDT.after(dia) && dateFromLocalDT.before(data_fim_dia)) {
                    contador += 1;
                    ordersVerified.add(o);
                }
            }
            
            for (Order o: ordersVerified) {
                orders.remove(o);
            }
            ordersVerified = new ArrayList<>();
            ordersLast7Days.add(contador);
        }
        
        return ordersLast7Days;
    }

    public ArrayList<Integer> getOrdersByWeight() throws IOException, InterruptedException {
        ArrayList<Integer> ordersByWeight = new ArrayList<Integer>();
        Map<Double, Double> weights = new TreeMap<Double, Double>();
        weights.put(0.0, 5.0);
        weights.put(5.0, 15.0);
        weights.put(15.0, 30.0);
        weights.put(30.0, 10000.0);

        ArrayList<Order> orders = orderRepository.findAll();

        List<Order> ordersVerified = new ArrayList<>();

        for (Map.Entry<Double,Double> entry : weights.entrySet()) {
            Double initialWeight = entry.getKey();
            Double finalWeight = entry.getValue();

            int contador = 0;
            for (Order o : orders) {
                if (initialWeight < o.getWeight() && o.getWeight() <= finalWeight ) {
                    contador += 1;
                    ordersVerified.add(o);
                }
            }
            for (Order o: ordersVerified) {
                orders.remove(o);
            }
            ordersVerified = new ArrayList<>();
            ordersByWeight.add(contador);
        }        
        return ordersByWeight;
    }

    public ArrayList<Order> getPendingOrders(double max_capacity) throws IOException, InterruptedException {
        return this.orderRepository.findPendingOrders(max_capacity);
    }

    public int countOrders(String status) throws IOException, InterruptedException {
        return this.orderRepository.countOrders(status);
    }

    public ArrayList<Order> getRefusedOrders(long id) throws IOException, InterruptedException {
        return this.orderRepository.findRefusedOrders(id);
    }

    public Order getOrderById(long id) throws IOException, InterruptedException {
        return this.orderRepository.findByPk(id);
    }

    public Order saveOrder(Order order) {
        if (order.getDeliver_lat() == null || order.getDeliver_lng() == null || order.getPick_up_lat()==null || order.getPick_up_lng()==null ||
            order.getApp_name() == null || order.getWeight() <= 0) return null;
        order = this.orderRepository.save(order);
        return order;
    }

    public Order updateRider(long order_id, long rider_id) {
        Order order = this.orderRepository.findByPk(order_id);
        order.setRider_id(rider_id);
        order.setStatus("Delivering");
        return this.orderRepository.save(order);
    }
}