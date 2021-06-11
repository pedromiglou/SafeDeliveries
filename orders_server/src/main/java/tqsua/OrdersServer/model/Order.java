package tqsua.OrdersServer.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "Orders")
public class Order{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "pick_up_lat", nullable = false)
    private Double pick_up_lat;

    @Column(name = "pick_up_lng", nullable = false)
    private Double pick_up_lng;

    @Column(name = "deliver_lat", nullable = false)
    private Double deliver_lat;

    @Column(name = "deliver_lng", nullable = false)
    private Double deliver_lng;

    /*
    @Column(name = "creation_date", nullable = false)
    private Date creation_date;
    */

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "rating")
    private int rating;

    @CreationTimestamp
    @Column(name="creation_date", updatable = false)
    private LocalDateTime creation_date;

    @JsonManagedReference
    @OneToMany(mappedBy="order")
    private Set<Item> items;

    @Column(name = "user_id")
    private long user_id;

    public Order() {
    }
    
    public Order(Double pick_up_lat, Double pick_up_lng, Double deliver_lat, Double deliver_lng, String status, long user_id) {
        this.user_id = user_id;
        this.pick_up_lat = pick_up_lat;
        this.pick_up_lng = pick_up_lng;
        this.deliver_lat = deliver_lat;
        this.deliver_lng = deliver_lng;
        this.status = status;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public LocalDateTime getCreation_date() {
        return this.creation_date;
    }
    

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }



    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", pick_up_lat='" + getPick_up_lat() + "'" +
            ", pick_up_lng='" + getPick_up_lng() + "'" +
            ", deliver_lat='" + getDeliver_lat() + "'" +
            ", deliver_lng='" + getDeliver_lng() + "'" +
            ", creation_date='" + getCreation_date() + "'" +
            ", status='" + getStatus() + "'" +
            ", rating='" + getRating() + "'" +
            //", items='" + getItems() + "'" +
            "}";
    }


}
