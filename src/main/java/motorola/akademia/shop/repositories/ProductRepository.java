package motorola.akademia.shop.repositories;

import motorola.akademia.shop.entities.Cart;
import motorola.akademia.shop.entities.Category;
import motorola.akademia.shop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);
    Optional<Product> findByName(String name);
    List<Product> findAll();
    List<Product> findAllByCategory(Category category);

}
