package tqsua.DeliveriesServer.service;

import org.springframework.stereotype.Service;

import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.Vehicle;
import tqsua.DeliveriesServer.model.VehicleDTO;
import tqsua.DeliveriesServer.repository.VehicleRepository;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RiderService riderService;

    public ArrayList<Vehicle> getAllVehicles() throws IOException, InterruptedException {
        return this.vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(long id) {
        return this.vehicleRepository.findById(id);
    }

    public ArrayList<Vehicle> getVehiclesByRiderId(long id) {
        return this.vehicleRepository.findByRider(id);
    }

    public Vehicle saveVehicle(VehicleDTO v) {
        if (v.getBrand()==null) return null;
        if (v.getModel()==null) return null;
        if (v.getCategory()==null) return null;
        if (v.getCapacity()==null) return null;
        if (v.getRegistration()==null) return null;
        if (v.getRider()==null) return null;
        if (riderService.getRiderById(v.getRider())==null) return null;
        Vehicle vehicle = new Vehicle(v.getBrand(), v.getModel(), v.getCategory(), v.getCapacity(), v.getRegistration());
        vehicle.setRider(riderService.getRiderById(v.getRider()));
        return this.vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(long id, VehicleDTO v) {
        Vehicle vehicle = this.vehicleRepository.findById(id);
        if (vehicle==null) return null;
        if (v.getBrand()!=null) vehicle.setBrand(v.getBrand());
        if (v.getModel()!=null) vehicle.setModel(v.getModel());
        if (v.getCategory()!=null) vehicle.setCategory(v.getCategory());
        if (v.getCapacity()!=null) vehicle.setCapacity(v.getCapacity());
        if (v.getRegistration()!=null) vehicle.setRegistration(v.getRegistration());
        return this.vehicleRepository.save(vehicle);
    }

    public Long deleteVehicle(long id) {
        if (!this.vehicleRepository.existsById(id)) return null;
        this.vehicleRepository.deleteById(id);
        return id;
    }
}