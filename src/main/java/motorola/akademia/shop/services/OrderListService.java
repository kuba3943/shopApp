package motorola.akademia.shop.services;

import motorola.akademia.shop.repository.Cart;
import motorola.akademia.shop.repository.Order;
import motorola.akademia.shop.repository.User;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class OrderListService {

    List<Order> orders = new ArrayList<>();
    private int id = 1;

    public Order addOrder(Cart cart, User user){
        Order order = new Order(id,user,cart);
        id++;
        orders.add(order);
        return order;
    }

    public List<Order> all() {
        return orders;
    }

    public List<Order> getOrdersByUser (User user){
        List<Order> ordersByUser = new ArrayList<>();
        for (Order o : orders) {
            if (o.getUser().getUsername().equals(user.getUsername())){
                ordersByUser.add(o);
            }

        }
        return ordersByUser;
    }

    public Order getOrdersById (int id){
        Order order = null;
        for (Order o : orders) {
            if (o.getId()==id){
                order=o;
            }

        }
        return order;
    }

    public BigDecimal getTotalPriceFromOrder(Order order){
            return order.getCart().getItemMap().values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal specialOffer20(int id, double reduction){

        Order order = getOrdersById(id);
        BigDecimal totalPrice20 = new BigDecimal(BigInteger.valueOf(0));
        for (Map.Entry<Cart.Item, BigDecimal> entry : order.getCart().getItemMap().entrySet()) {
            if (entry.getKey().getQuantity()>=5){
                totalPrice20=totalPrice20.add(entry.getValue().multiply(BigDecimal.valueOf(reduction)));
               }
        }
        return totalPrice20;
    }

}
