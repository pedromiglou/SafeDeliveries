package tqsua.DeliveriesServer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@Table(name = "Vehicles")
public class Vehicle {

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
    @ManyToOne(optional = false)
    private Rider rider;

    public Vehicle() {
    }

    public Vehicle(String brand, String model, String category, Double capacity) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vehicle vehicle = (Vehicle) o;

        if (id != vehicle.id) return false;
        if (!Objects.equals(brand, vehicle.brand)) return false;
        if (!Objects.equals(model, vehicle.model)) return false;
        if (!Objects.equals(category, vehicle.category)) return false;
        return Objects.equals(capacity, vehicle.capacity);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (capacity != null ? capacity.hashCode() : 0);
        return result;
    }
}