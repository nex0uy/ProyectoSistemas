package Models;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author germanpujadas
 */
public class Restaurant extends Thread{

    public int rut;
    public int restaurantId;
    public LinkedList<Order> pendingOrders;
    public LinkedList<Order> priorityOrders;
    public LinkedList<Order> readyOrders;
    int maxTimeInQueue = 3;
    Instant lastProducedNonPriorityOrder = null;

    public Restaurant(
            int rut, int restaurantId) {
        this.rut = rut;
        this.restaurantId = restaurantId;
        this.pendingOrders = new LinkedList<Order>();
        this.priorityOrders = new LinkedList<Order>();
        this.readyOrders = new LinkedList<Order>();
    }

    Boolean AddNewOrder(Order order) {
        return order.customer.membership
                ? priorityOrders.add(order) : pendingOrders.add(order);
    }

    Order GetNextOrder() {
        long minutesElapsed = ChronoUnit.MINUTES.between(this.lastProducedNonPriorityOrder, Instant.now());
        if (this.pendingOrders.size() > 0
                && minutesElapsed > this.maxTimeInQueue) {
            return this.pendingOrders.removeFirst();
        } else {
            if (this.priorityOrders.size() > 0) {
                return this.priorityOrders.removeFirst();
            } else {
                if (this.pendingOrders.size() > 0) {
                    return this.pendingOrders.removeFirst();
                } else {
                    return null;
                }
            }
        }
    }

    public void makeNextOrder() {
        var nextOrder = this.GetNextOrder();
        this.readyOrders.add(nextOrder);
    }
    
    @Override
    public void run() {
        
    }
}
