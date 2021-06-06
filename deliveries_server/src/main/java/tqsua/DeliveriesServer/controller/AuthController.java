package tqsua.DeliveriesServer.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import tqsua.DeliveriesServer.service.RiderService;
import tqsua.DeliveriesServer.model.Rider;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AuthController {


    @Autowired
    private RiderService riderService;

    @PostMapping(path="/register")
    public ResponseEntity<HashMap<String, String>> registo(HttpServletRequest req, HttpServletResponse res) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
        
        // Create new rider
        Rider rider = new ObjectMapper().readValue(req.getInputStream(), Rider.class);

        // Verify if email is already in use
        if (Boolean.TRUE == riderService.existsRiderByEmail(rider.getEmail())) {

            HashMap<String, String> response = new HashMap<>();
            response.put("error", "Email is already in use!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  
        }

        // Save rider in repository
        riderService.saveRider(rider);
        
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Rider was registed with sucess!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);  
    }

}