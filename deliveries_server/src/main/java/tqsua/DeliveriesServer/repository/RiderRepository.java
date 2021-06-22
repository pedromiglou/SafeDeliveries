package tqsua.DeliveriesServer.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tqsua.DeliveriesServer.model.Rider;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long>{

	ArrayList<Rider> findAll();
	
	@Query(value = "Select DISTINCT riders.* from riders JOIN vehicles ON vehicles.rider_id = riders.id where status = \"Online\" and capacity > :weight and riders.id NOT IN (Select rider_id from notifications)", nativeQuery = true)
	ArrayList<Rider> findAvailableRiders(@Param("weight") Double weight);

	Rider findById(long id);

	Rider findByEmail(String email);

	boolean existsRiderByEmail(String email);

	@Query(value = "SELECT count(*) FROM riders where status = :state", nativeQuery = true)
	int countByState(@Param("state") String state);
}