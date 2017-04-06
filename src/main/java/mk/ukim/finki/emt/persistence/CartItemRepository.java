package mk.ukim.finki.emt.persistence;

import mk.ukim.finki.emt.model.jpa.CartItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Bojan on 4/6/2017.
 */
public interface CartItemRepository extends CrudRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {
}
