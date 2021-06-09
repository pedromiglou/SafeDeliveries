package tqsua.DeliveriesServer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompositeKey implements Serializable{

    @Column(name="rider_id")
    private long rider_id;

    @Column(name = "order_id", nullable = false)
    private long order_id;


    public CompositeKey() {
    }


    public CompositeKey(long rider_id, long order_id) {
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
            " rider_id='" + getRider_id() + "'" +
            ", order_id='" + getOrder_id() + "'" +
            "}";
    }

}