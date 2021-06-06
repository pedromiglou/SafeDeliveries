package tqsua.OrdersServer.controller;

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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import tqsua.OrdersServer.service.UserService;
import tqsua.OrdersServer.model.User;


@RestController
@RequestMapping("/api")
public class AuthController {
    
    @Autowired
    private UserService userService;

    @PostMapping(path="/register")
    public ResponseEntity<HashMap<String, String>> registo(HttpServletRequest req, HttpServletResponse res) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
        
        // Create new rider
        User user = new ObjectMapper().readValue(req.getInputStream(), User.class);

        // Verify if email is already in use
        if (Boolean.TRUE == userService.existsUserByEmail(user.getEmail())) {

            HashMap<String, String> response = new HashMap<>();
            response.put("error", "Email is already in use!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  
        }

        // Save rider in repository
        userService.saveUser(user);
        
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "User was registed with sucess!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);  
    }

}
