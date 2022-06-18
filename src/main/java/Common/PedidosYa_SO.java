package Common;

import Models.Delivery;
import Models.Deposit;
import Models.Order;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author germanpujadas
 */
public class PedidosYa_SO {

    public static void main(String[] args) {
        var loader = new InitialLoad();
        String basePath = "src/main/java/Files/";
        Deposit deposito = new Deposit();
        var restaurants = loader.LoadRestaurant(basePath+"Restaurant_TEST.csv",
                                                basePath+"Orders_TEST.csv",
                                                basePath+"Customers_TEST.csv",
                                                deposito);
        
        restaurants.stream().forEach(rest -> rest.start());
        
        for (int i = 0; i < 12; i++) {
            new Delivery(i, deposito).start();
        }
        var a = "";
    }
}
