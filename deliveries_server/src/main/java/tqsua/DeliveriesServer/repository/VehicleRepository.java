package tqsua.DeliveriesServer.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tqsua.DeliveriesServer.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>{
    ArrayList<Vehicle> findAll();

    Vehicle findById(long id);


    @Query(value = "Select * from vehicles where rider_id like :id", nativeQuery = true)
    ArrayList<Vehicle> findByRider(@Param("id") Long id);
}