package Models;

import java.time.LocalDateTime;

/**
 *
 * @author germanpujadas
 */
public class Order {

    public int orderId;
    public Customer customer;
    public int restaurantId;
    public LocalDateTime orderTime;
    public int moment;

    public Order(
            int orderId, Customer customer, int restaurantId, LocalDateTime orderTime, int moment) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurantId = restaurantId;
        this.orderTime = orderTime;
        this.moment = moment;
    }
}
