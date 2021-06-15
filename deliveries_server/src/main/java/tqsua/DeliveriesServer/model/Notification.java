package tqsua.DeliveriesServer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Notifications")
public class Notification {

    @Id
    @Column(name="rider_id")
    private long rider_id;

    @Column(name = "order_id", nullable = false)
    private long order_id;

    public Notification() {
    }

    public Notification(long rider_id, long order_id) {
        this.rider_id = rider_id;
        this.order_id = order_id;
    }

    public long getRider_id() {
        return this.rider_id;
    }

    public void setRider_id(long rider_id) {
        this.rider_id = rider_id;
    }

    public long getOrder_id() {
        return this.order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }


    @Override
    public String toString() {
        return "{" +
            ", rider_id='" + getRider_id() + "'" +
            ", order_id='" + getOrder_id() + "'" +
            "}";
    }

}
