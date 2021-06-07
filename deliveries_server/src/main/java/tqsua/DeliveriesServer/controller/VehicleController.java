package tqsua.DeliveriesServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqsua.DeliveriesServer.model.Vehicle;
import tqsua.DeliveriesServer.model.VehicleDTO;
import tqsua.DeliveriesServer.service.RiderService;
import tqsua.DeliveriesServer.service.VehicleService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private RiderService riderService;

    @GetMapping(path="/vehicles")
    public ArrayList<Vehicle> getVehicles() throws IOException, InterruptedException {
        return this.vehicleService.getAllVehicles();
    }

    @GetMapping(path="/vehicle")
    public Vehicle getVehicleById(@RequestParam(name="id") long id) {
        Vehicle v = this.vehicleService.getVehicleById(id);
        if (v==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return v;
    }

    @PostMapping(path = "/vehicle")
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle createVehicle(@Valid @RequestBody VehicleDTO v) {
        Vehicle vehicle = this.vehicleService.saveVehicle(v);
        if (vehicle==null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return vehicle;
    }

    @PutMapping(path="/vehicle/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable(value="id") Long id, @Valid @RequestBody VehicleDTO v) {
        Vehicle vehicle = this.vehicleService.updateVehicle(id, v);
        if (vehicle==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(vehicle);
    }

    @DeleteMapping(path="/vehicle/{id}")
    public void deleteVehicle(@PathVariable(value="id") Long id) {
        if (this.vehicleService.deleteVehicle(id)==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}