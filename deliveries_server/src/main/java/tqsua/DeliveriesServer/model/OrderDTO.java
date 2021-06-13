package tqsua.DeliveriesServer.model;

public class OrderDTO {

    private Double pick_up_lat;

    private Double pick_up_lng;

    private Double deliver_lat;

    private Double deliver_lng;

    private Double weight;

    private String app_name;

    public OrderDTO() {
    }

    public OrderDTO(Double pick_up_lat, Double pick_up_lng, Double deliver_lat, Double deliver_lng, Double weight, String app_name) {
        this.pick_up_lat = pick_up_lat;
        this.pick_up_lng = pick_up_lng;
        this.deliver_lat = deliver_lat;
        this.deliver_lng = deliver_lng;
        this.weight = weight;
        this.app_name = app_name;
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
            " pick_up_lat='" + getPick_up_lat() + "'" +
            ", pick_up_lng='" + getPick_up_lng() + "'" +
            ", deliver_lat='" + getDeliver_lat() + "'" +
            ", deliver_lng='" + getDeliver_lng() + "'" +
            ", weight='" + getWeight() + "'" +
            ", app_name='" + getApp_name() + "'" +
            "}";
    }


}
