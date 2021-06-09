package tqsua.OrdersServer.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqsua.OrdersServer.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	ArrayList<User> findAll();

	User findByEmail(String email);

	boolean existsUserByEmail(String email);
}