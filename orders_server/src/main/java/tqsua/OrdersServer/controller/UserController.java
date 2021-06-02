package tqsua.OrdersServer.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqsua.OrdersServer.model.User;
import tqsua.OrdersServer.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path="/users")
    public ArrayList<User> getAllUsers() throws IOException, InterruptedException {
        return userService.getAllUsers();
    }

}