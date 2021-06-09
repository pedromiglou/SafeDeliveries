package tqsua.OrdersServer.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqsua.OrdersServer.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{

	ArrayList<Item> findAll();

}