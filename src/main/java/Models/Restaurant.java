package Models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

public class Restaurant extends Thread {

    public int restaurantId; //Id de restaurant
    public LinkedList<Order> pendingOrders; //LIsta de pedidos pendientes
    public LinkedList<Order> priorityOrders; //Lista de pedidos prioritarios
    int maxTimeInQueue = 3; //Tiempo máximo de espera de pedido no prioritario
    Instant lastProducedNonPriorityOrder; //Última fecha de proceso de pedido no prioritario, se usa para controlar que el tiempo de espera no supere el máximo establecido en maxTimeInQueue

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

    /*
    Lógica para obtener el próximo pedido a despachar con los siguientes criterios:
        1. Existen pedidos cuyo tiempo de espera excede al máximo.
        2. Existen pedidos prioritarios sin procesar y no existen pedidos no prioritarios cuyo tiempo de espera excede al máximo.
        3. No existen pedidos prioritarios y hay pedidos no prioritarios pendientes.
    */
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
                deposito.addOrder(nextOrder);
                if (!nextOrder.customer.membership) { //Si se procesó un pedido no prioritario, actualizo la fecha de última entrega de este tipo de pedidos
                    this.lastProducedNonPriorityOrder = Instant.now();
                }
            }
        }
    }
}
