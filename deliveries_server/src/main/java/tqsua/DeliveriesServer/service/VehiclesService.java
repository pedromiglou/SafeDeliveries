package tqsua.DeliveriesServer.service;

import org.springframework.stereotype.Service;

import tqsua.DeliveriesServer.model.Vehicles;
import tqsua.DeliveriesServer.repository.VehiclesRepository;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VehiclesService {

    @Autowired
    private VehiclesRepository vehiclesRepository;

    public ArrayList<Vehicles> getAllVehicles() throws IOException, InterruptedException {
        return this.vehiclesRepository.findAll();
    }

}