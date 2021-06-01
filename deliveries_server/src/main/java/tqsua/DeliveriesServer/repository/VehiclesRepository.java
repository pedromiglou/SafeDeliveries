package tqsua.DeliveriesServer.repository;


import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqsua.DeliveriesServer.model.Vehicles;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles, Long>{

    ArrayList<Vehicles> findAll();

}