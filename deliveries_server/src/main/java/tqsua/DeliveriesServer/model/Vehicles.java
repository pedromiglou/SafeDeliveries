package tqsua.DeliveriesServer.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "Vehicles")
public class Vehicles implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "capacity", nullable = false)
    private Double capacity;

    // pertence a um rider
    @ManyToOne
    private Rider rider;

    public Vehicles() {
    }


    public Vehicles(String brand, String model, String category, Double capacity) {
        this.brand = brand;
        this.model = model;
        this.category = category;
        this.capacity = capacity;
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getCapacity() {
        return this.capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Rider getRider() {
        return this.rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", category='" + getCategory() + "'" +
            ", capacity='" + getCapacity() + "'" +
            "}";
    }
    
}