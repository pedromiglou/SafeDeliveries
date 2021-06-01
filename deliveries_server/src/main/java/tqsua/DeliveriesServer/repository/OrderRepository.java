package tqsua.DeliveriesServer.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqsua.DeliveriesServer.model.CompositeKey;
import tqsua.DeliveriesServer.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, CompositeKey>{
    
	ArrayList<Order> findAll();

}