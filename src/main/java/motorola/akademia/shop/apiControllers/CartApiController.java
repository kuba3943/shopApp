package motorola.akademia.shop.apiControllers;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.repository.Cart;
import motorola.akademia.shop.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/shop")
public class CartApiController {

    private final CartService cartService;

    @PostMapping("/addItemToCart")
    public ResponseEntity<Cart> addItemToCart(@RequestParam int productId, @RequestParam int quantity){
        return ResponseEntity.ok(cartService.addProductToCart(productId,quantity));
    }

    @GetMapping ("/clearCart")
    public ResponseEntity<Cart> clearCart(){
        return ResponseEntity.ok(cartService.clearCart());
    }

    @GetMapping ("/getCart")
    public ResponseEntity<Map<Cart.Item, BigDecimal>> getCart(){
        return ResponseEntity.ok(cartService.all());
    }

    @PostMapping("/deleteItemFromCart")
    public ResponseEntity<Cart> deteteItemFromCart(@RequestParam int productId){
        return ResponseEntity.ok(cartService.deleteProductFromCart(productId));
    }

    @PatchMapping("/changeQuantityOfItem")
    public ResponseEntity<Cart> changeQiantityOfItem(@RequestParam int productId, @RequestParam int newQuantity){
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
