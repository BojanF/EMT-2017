package mk.ukim.finki.emt.service;


import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.Cart;
import mk.ukim.finki.emt.model.jpa.CartItem;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Bojan on 4/6/2017.
 */
public interface CartServiceHelper {

    Cart createCart(LocalDateTime expiryDate, Double totalPrice);
    void removeCart(Long cartId);

    /*void addCartItem(Long cartId, Long bookId, int quantity);
    void removeCartItem(Long cartId, Long bookId);*/

    CartItem addCartItem(Cart cart, Book book, int quantity);
    void removeCartItem(Cart cart, CartItem cartItem);

    Cart addToTotalPrice(Cart cart, Double price);
    Cart subtractFromTotalPrice(Cart cart, Double price);


    /*int getBookQuatity(CartItem item);
    Double getBookPrice(Book book);*/
    Double getCartItemPrice(Cart cart, CartItem cartItem);
    void changeCartItemQuantity(Cart cart, CartItem cartItem);


    List<CartItem> getCartItems(Cart cart);
    Long getCartTotal(Cart cart);
    int getCartItemsQuantity(Cart cart);




}
