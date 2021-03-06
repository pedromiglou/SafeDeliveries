package tqsua.OrdersServer.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tqsua.OrdersServer.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	ArrayList<Order> findAll();

	@Query(value = "Select * from orders where deliver_id = :id", nativeQuery = true)
	Order getOrderByDeliverId(@Param("id") long id);

	@Query(value = "Select * from orders where user_id = :user_id order by creation_date desc", nativeQuery = true)
	ArrayList<Order> getOrdersByUserId(@Param("user_id") long user_id);
}