package tqsua.DeliveriesServer.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Orders")
public class Order implements Serializable{

    @EmbeddedId CompositeKey OrderId;

    public Order() {
    }
    

    public Order(CompositeKey OrderId) {
        this.OrderId = OrderId;
    }


    public CompositeKey getOrderId() {
        return this.OrderId;
    }

    public void setOrderId(CompositeKey OrderId) {
        this.OrderId = OrderId;
    }


    @Override
    public String toString() {
        return "{" +
            " OrderId='" + getOrderId() + "'" +
            "}";
    }
    

}
