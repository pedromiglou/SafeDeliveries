package tqsua.OrdersServer.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqsua.OrdersServer.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	ArrayList<Order> findAll();

}