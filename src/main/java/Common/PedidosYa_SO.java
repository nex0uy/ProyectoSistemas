package Common;

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
        var restaurants = loader.LoadRestaurant(basePath+"Restaurant_TEST.csv",
                                                basePath+"Orders_TEST.csv",
                                                basePath+"Customers_TEST.csv");
        
        var orders = loader.LoadOrders(basePath+"Orders_TEST.csv",
                                       basePath+"Customers_TEST.csv");
        
        Map<Integer, List<Order>> groupedList =
                        orders.stream().collect(Collectors.groupingBy(order -> order.restaurantId));
        restaurants.stream().forEach(rest -> rest.run());
        restaurants.stream().forEach(rest -> groupedList.get(rest.restaurantId)
                                                        .stream().forEach(ord -> rest.AddNewOrder(ord)));
        var a = "";
    }
}
