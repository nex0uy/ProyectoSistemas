package Models;

import java.time.LocalDateTime;

public class Order {

    public int orderId; //Número de pedido
    public Customer customer; //Cliente del pedido
    public int restaurantId; //Id del restaurant
    public LocalDateTime orderTime; //Hora de realización del pedido
    public int moment; //Momento en el que se realiza el pedido

    public Order(
            int orderId, Customer customer, int restaurantId, LocalDateTime orderTime, int moment) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurantId = restaurantId;
        this.orderTime = orderTime;
        this.moment = moment;
    }
}
