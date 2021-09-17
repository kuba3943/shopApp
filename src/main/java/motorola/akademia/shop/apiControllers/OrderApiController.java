package motorola.akademia.shop.apiControllers;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.repository.Cart;
import motorola.akademia.shop.repository.Order;
import motorola.akademia.shop.services.OrderListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/shop")
public class OrderApiController {

    private final OrderListService orderListService;

    @GetMapping("/getAllOrder")
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(orderListService.all());
    }

    @GetMapping("/getOrdersByUser/{username}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable String username){
        return ResponseEntity.ok(orderListService.getOrdersByUser(username));
    }

    @GetMapping("/getOrderById/{id}")
    public ResponseEntity<Order> getOrdersByUser(@PathVariable int id){
        return ResponseEntity.ok(orderListService.getOrdersById(id));
    }


}
