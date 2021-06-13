package tqsua.DeliveriesServer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Orders")
public class Order implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long order_id;

    @Column(name="rider_id")
    private long rider_id;

    @Column(name = "pick_up_lat", nullable = false)
    private Double pick_up_lat;

    @Column(name = "pick_up_lng", nullable = false)
    private Double pick_up_lng;

    @Column(name = "deliver_lat", nullable = false)
    private Double deliver_lat;

    @Column(name = "deliver_lng", nullable = false)
    private Double deliver_lng;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "app_name", nullable = false)
    private String app_name;

    public Order() {
    }
    

    public Order(long rider_id, Double pick_up_lat, Double pick_up_lng, Double deliver_lat, Double deliver_lng, Double weight, String app_name) {
        this.rider_id = rider_id;
        this.pick_up_lat = pick_up_lat;
        this.pick_up_lng = pick_up_lng;
        this.deliver_lat = deliver_lat;
        this.deliver_lng = deliver_lng;
        this.weight = weight;
        this.app_name = app_name;
    }
    

    public long getOrder_id() {
        return this.order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public long getRider_id() {
        return this.rider_id;
    }

    public void setRider_id(long rider_id) {
        this.rider_id = rider_id;
    }


    public Double getPick_up_lat() {
        return this.pick_up_lat;
    }

    public void setPick_up_lat(Double pick_up_lat) {
        this.pick_up_lat = pick_up_lat;
    }

    public Double getPick_up_lng() {
        return this.pick_up_lng;
    }

    public void setPick_up_lng(Double pick_up_lng) {
        this.pick_up_lng = pick_up_lng;
    }

    public Double getDeliver_lat() {
        return this.deliver_lat;
    }

    public void setDeliver_lat(Double deliver_lat) {
        this.deliver_lat = deliver_lat;
    }

    public Double getDeliver_lng() {
        return this.deliver_lng;
    }

    public void setDeliver_lng(Double deliver_lng) {
        this.deliver_lng = deliver_lng;
    }

    public Double getWeight() {
        return this.weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getApp_name() {
        return this.app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }


    @Override
    public String toString() {
        return "{" +
            " OrderId='" + getOrder_id() + "'" +
            ", rider_id='" + getRider_id() + "'" +
            ", pick_up_lat='" + getPick_up_lat() + "'" +
            ", pick_up_lng='" + getPick_up_lng() + "'" +
            ", deliver_lat='" + getDeliver_lat() + "'" +
            ", deliver_lng='" + getDeliver_lng() + "'" +
            ", weight='" + getWeight() + "'" +
            ", app_name='" + getApp_name() + "'" +
            "}";
    }

    

}
