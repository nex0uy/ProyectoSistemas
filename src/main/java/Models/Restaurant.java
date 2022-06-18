package Models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

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

    public Deposit deposito;

    public Restaurant(
            int restaurantId, Deposit deposito) {
        this.restaurantId = restaurantId;
        this.pendingOrders = new LinkedList<Order>();
        this.priorityOrders = new LinkedList<Order>();
        this.lastProducedNonPriorityOrder = Instant.now();
        this.deposito = deposito;
    }

    public Boolean AddNewOrder(Order order) {
        Boolean result = false;
        result = order.customer.membership
                ? priorityOrders.add(order) : pendingOrders.add(order);

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

    public void run() {

        while (true) {
            var nextOrder = this.GetNextOrder();
            if (nextOrder != null) {
                deposito.agregarOrden(nextOrder);
                if (!nextOrder.customer.membership) {
                    this.lastProducedNonPriorityOrder = Instant.now();
                }
            }
        }
    }
}
