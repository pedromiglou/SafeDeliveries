package tqsua.DeliveriesServer.model;

import java.util.Objects;

public class RiderDTO {
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Double rating;
    private String status;
    private Double lat;
    private Double lng;

    public RiderDTO() {
    }

    public RiderDTO(String firstname, String lastname, String email, String password, Double rating, String status, Double lat, Double lng) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.rating = rating;
        this.status = status;
        this.lat = lat;
        this.lng = lng;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getRating() {
        return this.rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLat() {
        return this.lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return this.lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RiderDTO)) {
            return false;
        }
        RiderDTO riderDTO = (RiderDTO) o;
        return id == riderDTO.id && Objects.equals(firstname, riderDTO.firstname) && Objects.equals(lastname, riderDTO.lastname) && Objects.equals(email, riderDTO.email) && Objects.equals(password, riderDTO.password) && Objects.equals(rating, riderDTO.rating) && Objects.equals(status, riderDTO.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, email, password, rating, status);
    }


}
