package tqsua.OrdersServer.model;

import java.time.LocalDateTime;
import java.util.Set;

public class OrderDTO {
    
    private Double pick_up_lat;

    private Double pick_up_lng;

    private Double deliver_lat;

    private Double deliver_lng;

    private String status;

    private int rating;

    private LocalDateTime creation_date;

    private Set<Item> items;

    private long user_id;


    public OrderDTO() {
    }


    public OrderDTO(Double pick_up_lat, Double pick_up_lng, Double deliver_lat, Double deliver_lng, String status, long user_id) {
        this.user_id = user_id;
        this.pick_up_lat = pick_up_lat;
        this.pick_up_lng = pick_up_lng;
        this.deliver_lat = deliver_lat;
        this.deliver_lng = deliver_lng;
        this.status = status;
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

    public LocalDateTime getCreation_date() {
        return this.creation_date;
    }

    public void setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }

    public Set<Item> getItems() {
        return this.items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }


    @Override
    public String toString() {
        return "{" +
            " pick_up_lat='" + getPick_up_lat() + "'" +
            ", pick_up_lng='" + getPick_up_lng() + "'" +
            ", deliver_lat='" + getDeliver_lat() + "'" +
            ", deliver_lng='" + getDeliver_lng() + "'" +
            ", status='" + getStatus() + "'" +
            ", rating='" + getRating() + "'" +
            ", creation_date='" + getCreation_date() + "'" +
            ", items='" + getItems() + "'" +
            ", user_id='" + getUser_id() + "'" +
            "}";
    }


}
