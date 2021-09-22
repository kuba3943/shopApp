package motorola.akademia.shop.repositories;

import motorola.akademia.shop.entities.Order;
import motorola.akademia.shop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findById(Long id);
    List<Order> findAll();
    List<Order> findAllByUser(User user);



}
