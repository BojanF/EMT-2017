package mk.ukim.finki.emt.persistence;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.Cart;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Bojan on 4/6/2017.
 */
@Repository
public interface CartRepository extends CrudRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
}
