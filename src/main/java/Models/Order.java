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

    public Order(
        int orderId, Customer customer, int restaurantId, LocalDateTime orderTime)
    {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurantId = restaurantId;
        this.orderTime = orderTime;
    }
}
