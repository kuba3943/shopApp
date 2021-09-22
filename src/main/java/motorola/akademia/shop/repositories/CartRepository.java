package motorola.akademia.shop.repositories;

import motorola.akademia.shop.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findById(Long id);

}
