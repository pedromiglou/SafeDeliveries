package tqsua.DeliveriesServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    private static final String message = "message";
    private static final String unauthorized = "Unauthorized";

    @GetMapping(path="/private/vehicles")
    public ArrayList<VehicleDTO> getVehicles(Authentication authentication) throws IOException, InterruptedException {
        // TODO:
        // Verify Admin
        ArrayList<VehicleDTO> vehicles = new ArrayList<>();
        for (Vehicle vehicle: this.vehicleService.getAllVehicles()) {
            vehicles.add(new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getCategory(), vehicle.getCapacity(), vehicle.getRider().getId(), vehicle.getRegistration()));
        }
        return vehicles;
    }

    @GetMapping(path="/private/vehicle")
    public ResponseEntity<Object> getVehicleById(Authentication authentication, @RequestParam(name="id") long id) {
        String rider_id = authentication.getName();
        Vehicle vehicle = this.vehicleService.getVehicleById(id);

        if (vehicle == null) {
            HashMap<String, String> response = new HashMap<>();
            response.put(message, "Not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (!rider_id.equals(String.valueOf(vehicle.getRider().getId()))) {
            HashMap<String, String> response = new HashMap<>();
            response.put(message, unauthorized);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        VehicleDTO response = new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getCategory(), vehicle.getCapacity(), vehicle.getRider().getId(), vehicle.getRegistration());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path="/private/rider/{id}/vehicles")
    public ResponseEntity<Object> getVehiclesByRiderId(Authentication authentication ,@PathVariable(value="id") long id) {
        String rider_id = authentication.getName();

        if (!rider_id.equals(String.valueOf(id))) {
            HashMap<String, String> response = new HashMap<>();
            response.put(message, unauthorized);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        ArrayList<Vehicle> vehicleList = this.vehicleService.getVehiclesByRiderId(id);
        ArrayList<VehicleDTO> response = new ArrayList<>();
        for (Vehicle vehicle: vehicleList) {
            response.add(new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getCategory(), vehicle.getCapacity(), vehicle.getRider().getId(), vehicle.getRegistration()));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/private/vehicle")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createVehicle(Authentication authentication ,@Valid @RequestBody VehicleDTO v) {
        String id = authentication.getName();

        if (!id.equals(String.valueOf(v.getRider()))) {
            HashMap<String, String> response = new HashMap<>();
            response.put(message, unauthorized);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Vehicle vehicle = this.vehicleService.saveVehicle(v);
        if (vehicle==null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        VehicleDTO response = new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getCategory(), vehicle.getCapacity(), vehicle.getRider().getId(), vehicle.getRegistration());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(path="/private/vehicle/{id}")
    public ResponseEntity<Object> updateVehicle(Authentication authentication ,@PathVariable(value="id") Long id, @Valid @RequestBody VehicleDTO v) {
        String rider_id = authentication.getName();
        Vehicle vehicle_found = vehicleService.getVehicleById(id);
        if (vehicle_found == null) {
            HashMap<String, String> response = new HashMap<>();
            response.put(message, "Not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!rider_id.equals(String.valueOf(vehicle_found.getRider().getId()))) {
            HashMap<String, String> response = new HashMap<>();
            response.put(message, unauthorized);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        
        Vehicle vehicle = this.vehicleService.updateVehicle(id, v);
        VehicleDTO updatedVehicle = new VehicleDTO(vehicle.getId(), vehicle.getBrand(), vehicle.getModel(), vehicle.getCategory(), vehicle.getCapacity(), vehicle.getRider().getId(), vehicle.getRegistration());
        return new ResponseEntity<>(updatedVehicle, HttpStatus.OK);
    }

    @DeleteMapping(path="/private/vehicle/{id}")
    public ResponseEntity<Object> deleteVehicle(Authentication authentication ,@PathVariable(value="id") Long id) {
        String rider_id = authentication.getName();
        Vehicle v = this.vehicleService.getVehicleById(id);
        if (v == null) {
            HashMap<String, String> response = new HashMap<>();
            response.put(message, "Not found.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if (!rider_id.equals(String.valueOf(v.getRider().getId()))) {
            HashMap<String, String> response = new HashMap<>();
            response.put(message, unauthorized);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        if (this.vehicleService.deleteVehicle(id)==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}