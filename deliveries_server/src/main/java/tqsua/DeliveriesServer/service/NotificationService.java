package tqsua.DeliveriesServer.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqsua.DeliveriesServer.model.Notification;
import tqsua.DeliveriesServer.repository.NotificationRepository;


@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification getNotificationByRider(long id) throws IOException, InterruptedException {
        return this.notificationRepository.getByRiderId(id);
    }

    public Notification save(Notification notification) {
        return this.notificationRepository.save(notification);
    }

    public Long delete(long rider_id) {
        if (!this.notificationRepository.existsById(rider_id)) return null;
        this.notificationRepository.deleteById(rider_id);
        return rider_id;
    }
    

}