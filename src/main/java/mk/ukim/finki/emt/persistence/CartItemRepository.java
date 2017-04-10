package mk.ukim.finki.emt.persistence;

import mk.ukim.finki.emt.model.jpa.CartItem;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Bojan on 4/6/2017.
 */
@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Long> {


    List<CartItem> findByCartId(Long id);
}
