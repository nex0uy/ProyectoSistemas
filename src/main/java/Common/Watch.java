package Common;

import Helpers.Logger;
import Models.Delivery;
import Models.Deposit;
import Models.Order;
import Models.Restaurant;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

public class Watch extends Thread {

    private static final Watch watch = new Watch(); //Instancia del controlador de momentos
    private final Logger logger = new Logger(); //Escribe en el archivo Log.txt
    private final InitialLoad loader = new InitialLoad(); //Carga inicial de datos

    private LinkedList<Restaurant> restaurants;
    private LinkedList<Order> orders;
    private String basePath = "src/main/java/Files/";
    
    private static final int incrementTime = 6; //Tiempo de incremento de momentos en segundos
    Instant lastIncrementedInstant = Instant.now(); //Última vez que se realizó un incremento de momento
    private int moment; //Contador de momentos
    private final int availableDeliverys = 10; //# de deliverys contratados

    public static Watch getWatch() {
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
        this.restaurants = loader.LoadRestaurant(basePath + "Restaurant_TEST.csv", deposito); //Obtiene los restaurantes del archivo
        this.orders = loader.LoadOrders(basePath + "Orders_TEST.csv", basePath + "Customers_TEST.csv"); //Obtiene los pedidos y clientes del archivo
        restaurants.stream().forEach(rest -> rest.start()); //Inicio (abro) el cada restaurant

        for (int i = 0; i < this.availableDeliverys; i++) {
            new Delivery(i, deposito).start(); //Genera instancias por cada delivery
        }
        while (true) {
            //Tiempo transcurrido entre el momento actual y la última vez que se incrementó un momento
            var elapsedTime = ChronoUnit.SECONDS.between(this.lastIncrementedInstant, Instant.now());
            if (elapsedTime >= this.incrementTime) { //Si el tiempo transcurrido es mayor o igual al tiempo definido para cada momento, incremento
                this.incrementCounter(); //Incrementa contador de momentos
                this.logger.addLine("Inicia el momento: " + this.moment);
                //Agrego los pedidos del momento correspondiente al restaurant indicado en el pedido
                this.restaurants.stream().forEach(rest -> {
                    this.orders.stream().filter(ord -> ord.moment == this.moment) //Filtro los pedidos del momento actual
                            .filter(ord -> ord.restaurantId == rest.restaurantId) //Filtro los pedidos del restaurant de la iteración
                            .forEach(ord -> rest.AddNewOrder(ord)); //Agrego el pedido al restaurant
                });
            }
        }
    }
}
