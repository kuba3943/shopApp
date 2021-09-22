package motorola.akademia.shop.apiControllers;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.entities.Cart;
import motorola.akademia.shop.entities.Product;
import motorola.akademia.shop.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/shop")
public class CartApiController {

    private final CartService cartService;

    @PostMapping("/addItemToCart")
    public ResponseEntity<Cart> addItemToCart(@RequestParam Product product, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.addProductToCart(product,quantity));
    }

    @GetMapping ("/clearCart")
    public ResponseEntity<Cart> clearCart(){
        return ResponseEntity.ok(cartService.clearCart());
    }

    @GetMapping ("/getCart")
    public ResponseEntity<List<Cart.Item>> getCart(){
        return ResponseEntity.ok(cartService.all());
    }

    @PostMapping("/deleteItemFromCart")
    public ResponseEntity<Cart> deteteItemFromCart(@RequestParam Long productId){
        return ResponseEntity.ok(cartService.deleteProductFromCart(productId));
    }

    @PatchMapping("/changeQuantityOfItem")
    public ResponseEntity<Cart> changeQiantityOfItem(@RequestParam Long productId, @RequestParam int newQuantity){
        return ResponseEntity.ok(cartService.changeQuantityOfProduct(productId,newQuantity));
    }

    @GetMapping("totalPrice")
    public ResponseEntity<BigDecimal> getTotalPriceOfCart(){
        return ResponseEntity.ok(cartService.getTotalPriceOfCart());
    }

    @GetMapping("totalPriceAfterReduction")
    public ResponseEntity<BigDecimal> getTotalPriceOfCartAfterReduction(@RequestParam double reduction){
        return ResponseEntity.ok(cartService.specialOffer20(reduction));
    }






}
