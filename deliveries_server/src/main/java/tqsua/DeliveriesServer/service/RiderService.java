package tqsua.DeliveriesServer.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import tqsua.DeliveriesServer.model.Rider;
import tqsua.DeliveriesServer.model.RiderDTO;
import tqsua.DeliveriesServer.repository.RiderRepository;

@Service
public class RiderService {

    @Autowired
    private RiderRepository riderRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public ArrayList<Rider> getAllRiders() throws IOException, InterruptedException {
        return this.riderRepository.findAll();
    }

    public ArrayList<Rider> getAvailableRiders(Double weight) throws IOException, InterruptedException {
        return this.riderRepository.findAvailableRiders(weight);
    }

    public boolean existsRiderByEmail(String email) {
        return this.riderRepository.existsRiderByEmail(email);
    }

    public Rider getRiderById(long id) {
        return this.riderRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Rider saveRider(Rider rider) {
        rider.setPassword(bCryptPasswordEncoder.encode(rider.getPassword()));
        return this.riderRepository.save(rider);
    }

    public Rider updateRider(long id, RiderDTO newDetails) {
        Rider rider = this.riderRepository.findById(id);
        if (rider==null) return null;
        if (newDetails.getFirstname()!=null) rider.setFirstname(newDetails.getFirstname());
        if (newDetails.getLastname()!=null) rider.setLastname(newDetails.getLastname());
        if (newDetails.getEmail()!=null) rider.setEmail(newDetails.getEmail());
        if (newDetails.getPassword()!=null) rider.setPassword(newDetails.getPassword());
        if (newDetails.getRating()!=null) rider.setRating(newDetails.getRating());
        if (newDetails.getStatus()!=null) rider.setStatus(newDetails.getStatus());
        return this.riderRepository.save(rider);
    }

    public Rider changeStatus(long id, String status) {
        Rider rider = this.riderRepository.findById(id);
        if (rider==null) return null;
        rider.setStatus(status);
        return this.riderRepository.save(rider);
    }

    public int getRidersByState(String state) {
        if (!state.equals("Online") && !state.equals("Offline") && !state.equals("Delivering")) {
            return 0;
        }
        return this.riderRepository.countByState(state);
    }

}