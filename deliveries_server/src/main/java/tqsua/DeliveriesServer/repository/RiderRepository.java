package tqsua.DeliveriesServer.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqsua.DeliveriesServer.model.Rider;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long>{

	ArrayList<Rider> findAll();

	Rider findById(long id);

	Rider findByEmail(String email);

	boolean existsRiderByEmail(String email);
}