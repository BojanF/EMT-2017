package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.Cart;
import mk.ukim.finki.emt.model.jpa.CartItem;
import mk.ukim.finki.emt.model.jpa.Category;
import mk.ukim.finki.emt.persistence.BookRepository;
import mk.ukim.finki.emt.persistence.CartItemRepository;
import mk.ukim.finki.emt.persistence.CartRepository;
import mk.ukim.finki.emt.persistence.CategoryRepository;
import mk.ukim.finki.emt.service.impl.CartHelperImpl;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Priority;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private static boolean setUpfinished = false;
    private static List<Book> bookList;

    @Before
    public void createObjects(){
        if(setUpfinished)
            return;

        Category c = new Category();
        c.name = "Java programming";
        Category base = categoryRepository.save(c);
        Book book = serviceHelper.createBook("Java essentials", base.id, new String[]{"Joshua Bloch"}, "1a", 100d);
        Book book2 = serviceHelper.createBook("Effective Java", base.id, new String[]{"Joshua Bloch"}, "1b", 120d);
        Book book3 = serviceHelper.createBook("Thinking in Java", base.id, new String[]{"Bruce Eckel"}, "1c", 140d);
        bookList = new ArrayList<Book>();
        bookList.add(book);
        bookList.add(book2);
        bookList.add(book3);

        setUpfinished = true;
    }

    @Test
    public void testCreateDeleteCart(){
        Cart cart = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);
        Assert.assertNotNull("Cart is not null!",  cartRepository.findOne(cart.id));


        cartHelper.removeCart(cart.id);
        Assert.assertNull("Cart is null", cartRepository.findOne(cart.id));

    }

    @Test
    public void testAddDeleteCartItem(){

        Cart cart = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);
        CartItem cartItem = cartHelper.addCartItem(cart, bookList.get(1), 3);

        Assert.assertNotNull(cartItemRepository.findOne(cartItem.id));
        Assert.assertEquals(cartRepository.findOne(cart.id).id, cartItemRepository.findOne(cartItem.id).cart.id);

        Cart cart1 = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);
        Assert.assertNotEquals(cartRepository.findOne(cart1.id).id, cartItemRepository.findOne(cartItem.id).cart.id);

        cartHelper.removeCartItem(cart, cartItem);
        Assert.assertNull(cartItemRepository.findOne(cartItem.id));
    }

    @Test
    public void testUpdateCartTotalPrice(){
        Cart cart = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);
        Cart cartFromDB = cartHelper.updateCartTotalPrice(cart, 300d);

        Assert.assertNotEquals(0d, cartFromDB.totalPrice);
        Assert.assertEquals(300d, cartFromDB.totalPrice, 0d);
    }

    @Test
    public void testUpdateCartExpiryDate(){
        Cart cart = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);
        LocalDateTime oldExpiryDate = cart.expiryDate;
        LocalDateTime newExpiryDate = oldExpiryDate.plusDays(3);
        Cart cartFromDB = cartHelper.updateCartExpiryDate(cart, newExpiryDate);

        Assert.assertNotEquals(cartFromDB.expiryDate, oldExpiryDate);
        Assert.assertEquals(cartFromDB.expiryDate, newExpiryDate);
    }

    @Test
    public void testUpdateCartItemQuantity(){
        Cart cart = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);
        CartItem cartItem = cartHelper.addCartItem(cart, bookList.get(0), 3);

        Assert.assertEquals(3, cartItem.quantity);
        Assert.assertEquals(300d, cartRepository.findOne(cart.id).totalPrice, 0d);

        CartItem cartItemFromDB = cartHelper.updateCartItemQuantity(cart, cartItem, 2);

        Assert.assertEquals(2, cartItemFromDB.quantity);
        Assert.assertEquals(200d, cartRepository.findOne(cart.id).totalPrice, 0d);

    }

    @Test
    public void testGetCartItemPrice(){
        Cart cart = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);

        CartItem cartItem = cartHelper.addCartItem(cart, bookList.get(2), 4 );

        Assert.assertEquals(560d, cartHelper.getCartItemPrice(cartItem), 0d);
    }

    @Test
    public void testDeleteAllCartItems(){
        Cart cart = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);

        CartItem cartItem1 = cartHelper.addCartItem(cart, bookList.get(0), 1);
        CartItem cartItem2 = cartHelper.addCartItem(cart, bookList.get(1), 2);

        Assert.assertNotNull(cartItem1);
        Assert.assertNotNull(cartItem2);

        cartHelper.deleteAllCartItems(cart);

        Assert.assertNull(cartItemRepository.findOne(cartItem1.id));
        Assert.assertNull(cartItemRepository.findOne(cartItem2.id));

        Cart cart1 = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);
        CartItem cartItem3 = cartHelper.addCartItem(cart1, bookList.get(0), 5);
        Assert.assertNotNull(cartItem3);
        Assert.assertNotNull(cartItem3);
    }

    @Test
    public void testGetAllCartItems(){
        Cart cart = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);

        CartItem cartItem1 = cartHelper.addCartItem(cart, bookList.get(0), 1);
        CartItem cartItem2 = cartHelper.addCartItem(cart, bookList.get(1), 2);

        List<CartItem> cartItems = new ArrayList<CartItem>();
        cartItems.add(cartItem1);
        cartItems.add(cartItem2);

        Assert.assertEquals(cartItems, cartHelper.getAllCartItems(cart));

        cartHelper.deleteAllCartItems(cart);

        Assert.assertNotEquals(cartItems, cartHelper.getAllCartItems(cart));

    }

    @Test
    public void testGetCartItemsQuantity(){
        Cart cart = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);

        CartItem carItem1 = cartHelper.addCartItem(cart, bookList.get(2), 4 );
        CartItem carItem2 = cartHelper.addCartItem(cart, bookList.get(1), 3 );

        Assert.assertEquals(7, cartHelper.getCartItemsQuantity(cart));
        //Assert.assertEquals(7, cartHelper.getCartItemsQuantity(cartRepository.findOne(cart.id)));

    }

    @Test
    public void testEntireProcess(){
        Cart cart = cartHelper.createCart(LocalDateTime.now().plusMonths(1), 0d);

        CartItem cartItem1 = cartHelper.addCartItem(cart, bookList.get(0), 1);
        Assert.assertEquals(100d, cart.totalPrice, 0d);

        CartItem cartItem2 = cartHelper.addCartItem(cart, bookList.get(1), 2);
        Assert.assertEquals(240d, cartHelper.getCartItemPrice(cartItem2), 0d);
        Assert.assertEquals(340d, cart.totalPrice, 0d);
        Assert.assertEquals(3, cartHelper.getCartItemsQuantity(cart));

        cartHelper.updateCartItemQuantity(cart, cartItem2, 1);
        Assert.assertEquals(120d, cartHelper.getCartItemPrice(cartItem2), 0d);
        Assert.assertEquals(220d, cart.totalPrice, 0d);
        Assert.assertEquals(2, cartHelper.getCartItemsQuantity(cart));

        cartHelper.removeCartItem(cart, cartItem1);
        Assert.assertEquals(1, cartHelper.getCartItemsQuantity(cart));
        Assert.assertEquals(120d, cart.totalPrice, 0d);

        CartItem cartItem3 = cartHelper.addCartItem(cart, bookList.get(2), 3);
        Assert.assertEquals(420d, cartHelper.getCartItemPrice(cartItem3), 0d);
        Assert.assertEquals(540d, cart.totalPrice, 0d);
        Assert.assertEquals(4, cartHelper.getCartItemsQuantity(cart));

        cartHelper.removeCart(cart.id);
        Assert.assertNull(cartItemRepository.findOne(cartItem2.id));
        Assert.assertNull(cartItemRepository.findOne(cartItem3.id));
        Assert.assertNull(cartRepository.findOne(cart.id));



    }
}
