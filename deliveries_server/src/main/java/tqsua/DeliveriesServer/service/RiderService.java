package tqsua.DeliveriesServer.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.repository.RiderRepository;

@Service
public class RiderService {

    @Autowired
    private RiderRepository riderRepository;

    public ArrayList<Rider> getAllRiders() throws IOException, InterruptedException {
        return this.riderRepository.findAll();
    }

    public Rider getRiderById(long id) {
        return this.riderRepository.findById(id);
    }

    public void updateRider(long id, String firstname, String lastname, String email, String password, Double rating, Boolean status) {
        Rider rider = this.riderRepository.findById(id);
        if (firstname!=null) rider.setFirstname(firstname);
        if (lastname!=null) rider.setLastname(lastname);
        if (email!=null) rider.setEmail(email);
        if (password!=null) rider.setPassword(password);
        if (rating!=null) rider.setRating(rating);
        if (status!=null) rider.setStatus(status);
        this.riderRepository.save(rider);
    }
}