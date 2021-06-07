package tqsua.DeliveriesServer.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqsua.DeliveriesServer.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>{
    ArrayList<Vehicle> findAll();

    Vehicle findById(long id);
}