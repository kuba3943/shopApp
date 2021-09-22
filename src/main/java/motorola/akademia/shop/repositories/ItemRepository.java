package motorola.akademia.shop.repositories;

import motorola.akademia.shop.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Cart.Item, Long> {
}
