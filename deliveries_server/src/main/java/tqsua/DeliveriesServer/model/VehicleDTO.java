package tqsua.DeliveriesServer.model;

public class VehicleDTO {
    private Long id;
    private String brand;
    private String model;
    private String category;
    private Double capacity;
    private Long rider;
    private String registration;

    public VehicleDTO(Long id, String brand, String model, String category, Double capacity, Long rider, String registration) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.category = category;
        this.capacity = capacity;
        this.registration = registration;
        this.rider = rider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public Long getRider() {
        return rider;
    }

    public void setRider(Long rider) {
        this.rider = rider;
    }

    @Override
    public String toString() {
        return "VehicleDTO{" +
                "id='" + id + '\'' +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", category='" + category + '\'' +
                ", capacity=" + capacity +
                ", rider=" + rider +
                ", registration='" + registration + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VehicleDTO that = (VehicleDTO) o;

        if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;
        if (model != null ? !model.equals(that.model) : that.model != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (capacity != null ? !capacity.equals(that.capacity) : that.capacity != null) return false;
        if (rider != null ? !rider.equals(that.rider) : that.rider != null) return false;
        return registration != null ? registration.equals(that.registration) : that.registration == null;
    }

    @Override
    public int hashCode() {
        int result = brand != null ? brand.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (capacity != null ? capacity.hashCode() : 0);
        result = 31 * result + (rider != null ? rider.hashCode() : 0);
        result = 31 * result + (registration != null ? registration.hashCode() : 0);
        return result;
    }
}