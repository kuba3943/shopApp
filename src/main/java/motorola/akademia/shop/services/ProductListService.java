package motorola.akademia.shop.services;

import lombok.AllArgsConstructor;
import motorola.akademia.shop.entities.Category;
import motorola.akademia.shop.entities.Product;
import motorola.akademia.shop.repositories.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductListService {

    private final ProductRepository productRepository;

    public List<Product> allToCart() {
        return productRepository.findAll();
    }

    public List<Product> all() {
        return productRepository.findAll().stream().filter(a -> !a.getName().substring(0,3).equals("DEL")).collect(Collectors.toList());
    }

    public Product byName(String name) {
     return productRepository.findByName(name).orElseThrow(() -> new RuntimeException("Coudn't find product with that name"));
    }


    public Product productById(Long id){
      return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Coudn't find product with that id"));
    }

    public List<Product> getProductsByCategory (String category){

        Category cat = Category.valueOf(category);
        return productRepository.findAllByCategory(cat);
    }

    public Product addProduct (Product product){
        return productRepository.save(product);
    }

//    public Product deleteProductById (Long id){
//
//       Product productToDelete = productRepository.findById(id).orElseThrow();
//
//       productRepository.delete(productToDelete);
//
//       return productToDelete;
//    }


    public Product deleteProductById (Long id){

        Product detetedProduct = new Product();
        for (Product p : productRepository.findAll()) {
            if(p.getId().equals(id)){
                String oldName = p.getName();
                p.setName("DELETED_" + oldName);
                productRepository.save(p);
                detetedProduct=p;
            }
        }
        return detetedProduct;
    }


}
