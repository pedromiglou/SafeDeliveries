package tqsua.DeliveriesServer.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}