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

}