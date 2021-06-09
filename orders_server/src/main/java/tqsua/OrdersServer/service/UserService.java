package tqsua.OrdersServer.service;


import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import tqsua.OrdersServer.model.User;
import tqsua.OrdersServer.repository.UserRepository;

@Service
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    public ArrayList<User> getAllUsers() throws IOException, InterruptedException {
        return this.userRepository.findAll();
    }


    public boolean existsUserByEmail(String email) {
        return this.userRepository.existsUserByEmail(email);
    }

    @Transactional(rollbackFor = Exception.class)
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }
}