package tqsua.OrdersServer.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqsua.OrdersServer.model.User;
import tqsua.OrdersServer.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ArrayList<User> getAllUsers() throws IOException, InterruptedException {
        return this.userRepository.findAll();
    }

}