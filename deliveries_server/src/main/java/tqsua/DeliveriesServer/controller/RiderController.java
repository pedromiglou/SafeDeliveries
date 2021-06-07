package tqsua.DeliveriesServer.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.service.RiderService;

@RestController
@RequestMapping("/api")
public class RiderController {
    @Autowired
    private RiderService riderService;

    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path="/riders")
    public ArrayList<Rider> getAllRiders() throws IOException, InterruptedException {
        return riderService.getAllRiders();
    }

    @GetMapping(path="/rider")
    public Rider getRiderById(@RequestParam(name="id") long id) {
        Rider r = riderService.getRiderById(id);
        if (r==null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return r;
    }

    @PutMapping(path="/rider")
    public void updateRider(@RequestParam(name="id") long id, @RequestParam(name="firstname", required = false) String firstname,
                            @RequestParam(name="lastname", required = false) String lastname, @RequestParam(name="email", required = false) String email,
                            @RequestParam(name="password", required = false) String password, @RequestParam(name="rating", required = false) Double rating,
                            @RequestParam(name="status", required = false) String status) {
        riderService.updateRider(id, firstname, lastname, email, password, rating, status);
    }
}