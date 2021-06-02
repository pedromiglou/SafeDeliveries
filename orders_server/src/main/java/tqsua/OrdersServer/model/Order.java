package tqsua.OrdersServer.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Orders")
public class Order{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "pick_up_address", nullable = false)
    private String pick_up_address;

    @Column(name = "deliver_address", nullable = false)
    private String deliver_address;

    @Column(name = "creation_date", nullable = false)
    private Date creation_date;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "rating", nullable = false)
    private int rating;

    public Order() {
    }
    
    public Order(String pick_up_address, String deliver_address, Date creation_date, String status, int rating) {
        this.pick_up_address = pick_up_address;
        this.deliver_address = deliver_address;
        this.creation_date = creation_date;
        this.status = status;
        this.rating = rating;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPick_up_address() {
        return this.pick_up_address;
    }

    public void setPick_up_address(String pick_up_address) {
        this.pick_up_address = pick_up_address;
    }

    public String getDeliver_address() {
        return this.deliver_address;
    }

    public void setDeliver_address(String deliver_address) {
        this.deliver_address = deliver_address;
    }

    public Date getCreation_date() {
        return this.creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
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


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", pick_up_address='" + getPick_up_address() + "'" +
            ", deliver_address='" + getDeliver_address() + "'" +
            ", creation_date='" + getCreation_date() + "'" +
            ", status='" + getStatus() + "'" +
            ", rating='" + getRating() + "'" +
            "}";
    }
    
    

}
