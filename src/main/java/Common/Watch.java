package Common;

import Helpers.Logger;
import Models.Delivery;
import Models.Deposit;
import Models.Order;
import Models.Restaurant;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

/**
 *
 * @author germanpujadas
 */
public class Watch extends Thread {

    private static final Watch watch = new Watch();
    private Logger logger = new Logger();
    private LinkedList<Restaurant> restaurants;
    private LinkedList<Order> orders;
    private InitialLoad loader = new InitialLoad();
    private String basePath = "src/main/java/Files/";
    private static final int incrementTime = 6;
    Instant lastIncrementedInstant = Instant.now();
    private int moment;


    public static Watch getWatch(){
        return watch;
    }
    
    public Integer getCounter() {
        return this.moment;
    }

    public void incrementCounter() {
        this.moment++;
        this.lastIncrementedInstant = Instant.now();
    }

    @Override
    public void run() {
        Deposit deposito = new Deposit();
        this.restaurants = loader.LoadRestaurant(basePath + "Restaurant_TEST.csv",
                                                deposito);
        this.orders = loader.LoadOrders(basePath + "Orders_TEST.csv", basePath + "Customers_TEST.csv");
        restaurants.stream().forEach(rest -> rest.start());

        for (int i = 0; i < 40; i++) {
            new Delivery(i, deposito).start();
        }
        while (true) {
            var elapsedTime = ChronoUnit.SECONDS.between(this.lastIncrementedInstant, Instant.now());
            if (elapsedTime >= this.incrementTime) {
                this.incrementCounter();
                this.logger.addLine(String.format("Inicia el momento: %s", this.moment));
                this.restaurants.stream().forEach(rest -> {
                    this.orders.stream().filter(ord -> ord.moment == this.moment)
                                        .filter(ord -> ord.restaurantId == rest.restaurantId)
                                        .forEach(ord -> rest.AddNewOrder(ord));
                });
            }
        }
    }
}
