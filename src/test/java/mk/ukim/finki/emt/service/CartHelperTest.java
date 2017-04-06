package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.Cart;
import mk.ukim.finki.emt.model.jpa.CartItem;
import mk.ukim.finki.emt.model.jpa.Category;
import mk.ukim.finki.emt.persistence.CartItemRepository;
import mk.ukim.finki.emt.persistence.CartRepository;
import mk.ukim.finki.emt.persistence.CategoryRepository;
import mk.ukim.finki.emt.service.impl.CartHelperImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Priority;
import java.time.LocalDateTime;

/**
 * Created by Bojan on 4/6/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CartHelperTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartHelperImpl cartHelper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookServiceHelper serviceHelper;

    @Autowired
    private CartItemRepository cartItemRepository;

    /*static Cart cart;
    static Book book;
    static CartItem cartItem;*/


    @Test
    public void testCreateDeleteCart(){
        /*Cart cart = new Cart();
        cart.expiryDate = LocalDateTime.now();
        cart.totalPrice = 0d;

        cartRepository.save(cart);*/

        Cart cart = cartHelper.createCart(LocalDateTime.now(), 0d);
        Assert.assertNotNull("Cart is not null!",  cartRepository.findOne(cart.id));


        cartHelper.removeCart(cart.id);
        Assert.assertNull("Cart is null", cartRepository.findOne(cart.id));

    }



    @Test
    public void testAddDeleteCartItem(){
        //Cart cart = cartHelper.createCart(LocalDateTime.now(), 0d);

        Category c = new Category();
        c.name = "base";
        Category base = categoryRepository.save(c);
        Book book = serviceHelper.createBook("Java essentials", base.id, new String[]{"Joshua Bloch"}, "123", 100d);
        Cart cart = cartHelper.createCart(LocalDateTime.now(), 0d);
        CartItem cartItem = cartHelper.addCartItem(cart, book, 3);
        Assert.assertNotNull(cartItem);
        Assert.assertEquals(cart.id, cartItemRepository.findOne(cartItem.id).cart.id);


        cartHelper.removeCartItem(cart, cartItem);
        Assert.assertNull(cartItemRepository.findOne(cartItem.id));

    }


    @Test
    public void testAddToTotalPrice(){
        Cart cart = cartHelper.createCart(LocalDateTime.now(), 10d);
        cartHelper.addToTotalPrice(cart, 300d);

        Assert.assertNotEquals(10d, cart.totalPrice);
        Assert.assertEquals(310d, cart.totalPrice, 0d);
    }

    @Test
    public void testSubtractFromTotalPrice(){
        Cart cart = cartHelper.createCart(LocalDateTime.now(), 250d);
        cartHelper.subtractFromTotalPrice(cart, 150d);

        Assert.assertNotEquals(250d, cart.totalPrice);
        Assert.assertEquals(100d, cart.totalPrice, 0d);
    }


    /*@Test
    public void testDeleteCart(){
        cartHelper.removeCart(cart.id);
        Assert.assertNull("Cart is null", cartRepository.findOne(cart.id));
    }*/



}
