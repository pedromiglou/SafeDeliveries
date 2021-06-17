package tqsua.DeliveriesServer.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tqsua.DeliveriesServer.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    
	ArrayList<Order> findAll();

	@Query(value = "Select * from orders where order_id = :id", nativeQuery = true)
	Order findByPk(long id);

	@Query(value = "Select * from orders left join notifications on orders.order_id = notifications.order_id where orders.rider_id = 0 and notifications.rider_id is NULL and weight <= :max_capacity order by creation_date", nativeQuery = true)
	ArrayList<Order> findPendingOrders(double max_capacity);

	@Query(value = "SELECT orders.* FROM order_refused_riders JOIN orders on order_refused_riders.order_order_id = orders.order_id where refused_riders = :id", nativeQuery = true)
	ArrayList<Order> findRefusedOrders(long id);

	@Query(value = "SELECT count(*) FROM orders", nativeQuery = true)
	int countAll();

	@Query(value = "SELECT count(*) FROM orders WHERE status = :status", nativeQuery = true)
	int countOrders(String status);

}