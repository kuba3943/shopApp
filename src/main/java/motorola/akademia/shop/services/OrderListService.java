package motorola.akademia.shop.services;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.entities.Cart;
import motorola.akademia.shop.entities.Order;
import motorola.akademia.shop.entities.User;
import motorola.akademia.shop.repositories.CartRepository;
import motorola.akademia.shop.repositories.OrderRepository;
import motorola.akademia.shop.repositories.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OrderListService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public Order addOrder(Cart cart, User user){
        Order order = new Order(user,cart);
        orderRepository.save(order);
        return order;
    }

    public List<Order> all() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUser (String username){
     User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException());
        return orderRepository.findAllByUser(user);
    }

    public Order getOrdersById (Long id){
     return orderRepository.findById(id).orElseThrow(()->new RuntimeException());
    }

    public BigDecimal getTotalPriceFromOrder(Order order){
            return order.getCart().getItem().stream().map(Cart.Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public BigDecimal specialOffer20(Long id, double reduction){

        Order order = getOrdersById(id);
        BigDecimal totalPrice20 = new BigDecimal(BigInteger.valueOf(0));
        for ( Cart.Item i : order.getCart().getItem()) {
            if(i.getQuantity()>=5){
                totalPrice20=totalPrice20.add(i.getPrice().multiply(BigDecimal.valueOf(reduction)));
            } else {
                totalPrice20=totalPrice20.add(i.getPrice());
            }
        }
        return totalPrice20;
    }


    public List<Order> getAllOrdersWithProduct (Long id){
        List<Order> ordersWithProduct = new ArrayList<>();

        for (Order o : all()) {
            if (o.getCart().getItem().stream().anyMatch(a -> a.getProduct().getId().equals(id))){
                ordersWithProduct.add(o);
            }
        }
        return ordersWithProduct;
    }

}
