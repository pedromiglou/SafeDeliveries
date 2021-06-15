package tqsua.DeliveriesServer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tqsua.DeliveriesServer.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{
    
    @Query(value = "Select * from notifications where rider_id = :id", nativeQuery = true)
	Notification getByRiderId(@Param("id") long id);

}