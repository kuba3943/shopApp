package motorola.akademia.shop.apiControllers;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.entities.Product;
import motorola.akademia.shop.entities.User;
import motorola.akademia.shop.services.ProductListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/shop")
public class ProductApiController {

    private final ProductListService productListService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProduct(){
        return ResponseEntity.ok(productListService.all());
    }

    @GetMapping("/productByName")
    public ResponseEntity<Product> findProductByName(@RequestParam String name){
        return ResponseEntity.ok(productListService.byName(name));
    }

    @GetMapping("/productById")
    public ResponseEntity<Product> findProductById(@RequestParam Long id){
        return ResponseEntity.ok(productListService.productById(id));
    }

    @GetMapping("/allByCategory/category/{category}")
    public ResponseEntity<List<Product>> getAllByCategory(@PathVariable String category){
        return ResponseEntity.ok(productListService.getProductsByCategory(category));
    }

    @PostMapping("/addNewProduct")
    public ResponseEntity<Product> addNewProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productListService.addProduct(product));
    }

    @GetMapping("/deleteProduct")
    public ResponseEntity<Product> deleteProductById(@RequestParam Long id){
        return ResponseEntity.ok(productListService.deleteProductById(id));
    }




}
