package Models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 *
 * @author germanpujadas
 */
public class Restaurant extends Thread {

    public int restaurantId;
    public LinkedList<Order> pendingOrders;
    public LinkedList<Order> priorityOrders;
    int maxTimeInQueue = 3;
    Instant lastProducedNonPriorityOrder;
    static Semaphore semRest;
    static Semaphore semDelivery;

    public Restaurant(
            int restaurantId, int restCapacity, int delCapacity) {
        this.restaurantId = restaurantId;
        this.pendingOrders = new LinkedList<Order>();
        this.priorityOrders = new LinkedList<Order>();
        this.semRest = new Semaphore(restCapacity);
        this.semDelivery = new Semaphore(delCapacity);
        this.lastProducedNonPriorityOrder = Instant.now();
    }

    public Boolean AddNewOrder(Order order) {
        Boolean result = false;
        try {
            this.semRest.acquire();
            result = order.customer.membership
                    ? priorityOrders.add(order) : pendingOrders.add(order);
        } catch (InterruptedException ex) {
        }
        this.semDelivery.release();
        return result;
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
        try {
            var nextOrder = this.GetNextOrder();
            if (nextOrder != null && !nextOrder.customer.membership) {
                this.semDelivery.acquire();
                this.lastProducedNonPriorityOrder = Instant.now();
                this.semRest.release();
            }
        } catch (InterruptedException ex) {
        }
    }

    @Override
    public void run() {
        this.makeNextOrder();
    }
}
