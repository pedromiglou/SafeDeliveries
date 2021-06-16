package tqsua.DeliveriesServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import tqsua.DeliveriesServer.model.Vehicle;
import tqsua.DeliveriesServer.model.VehicleDTO;
import tqsua.DeliveriesServer.service.VehicleService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping(path="/vehicles")
    public ArrayList<VehicleDTO> getVehicles() throws IOException, InterruptedException {
        ArrayList<VehicleDTO> vehicles = new ArrayList<>();
        for (Vehicle vehicle: this.vehicleService.getAllVehicles()) {
            vehicles.add(new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getCategory(), vehicle.getCapacity(), vehicle.getRider().getId(), vehicle.getRegistration()));
        }
        return vehicles;
    }

    @GetMapping(path="/vehicles/statistics")
    public ResponseEntity<Object> getVehiclesStatistics() throws IOException, InterruptedException {
        HashMap<String, Object> response = new HashMap<>();
        ArrayList<Integer> vehiclesByCapacity = vehicleService.getVehiclesByCapacity();
        
        response.put("vehicles_by_capacity", vehiclesByCapacity);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path="/vehicle")
    public VehicleDTO getVehicleById(@RequestParam(name="id") long id) {
        Vehicle vehicle = this.vehicleService.getVehicleById(id);
        if (vehicle==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getCategory(), vehicle.getCapacity(), vehicle.getRider().getId(), vehicle.getRegistration());
    }

    @GetMapping(path="/vehiclesbyrider")
    public ArrayList<VehicleDTO> getVehiclesByRiderId(@RequestParam(name="id") long id) {
        ArrayList<Vehicle> vehicleList = this.vehicleService.getVehiclesByRiderId(id);
        ArrayList<VehicleDTO> response = new ArrayList<>();
        for (Vehicle vehicle: vehicleList) {
            response.add(new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getCategory(), vehicle.getCapacity(), vehicle.getRider().getId(), vehicle.getRegistration()));
        }
        return response;
    }

    @PostMapping(path = "/vehicle")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleDTO createVehicle(@Valid @RequestBody VehicleDTO v) {
        Vehicle vehicle = this.vehicleService.saveVehicle(v);
        if (vehicle==null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getCategory(), vehicle.getCapacity(), vehicle.getRider().getId(), vehicle.getRegistration());
    }

    @PutMapping(path="/vehicle/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable(value="id") Long id, @Valid @RequestBody VehicleDTO v) {
        Vehicle vehicle = this.vehicleService.updateVehicle(id, v);
        if (vehicle==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        VehicleDTO updatedVehicle = new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getCategory(), vehicle.getCapacity(), vehicle.getRider().getId(), vehicle.getRegistration());
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping(path="/vehicle/{id}")
    public void deleteVehicle(@PathVariable(value="id") Long id) {
        if (this.vehicleService.deleteVehicle(id)==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}