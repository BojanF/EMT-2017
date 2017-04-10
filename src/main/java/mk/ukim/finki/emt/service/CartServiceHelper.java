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

    CartItem addCartItem(Cart cart, Book book, int quantity);
    void removeCartItem(Cart cart, CartItem cartItem);

    Cart updateCartTotalPrice(Cart cart, Double price);
    Cart updateCartExpiryDate(Cart cart, LocalDateTime newExpiryDate);

    CartItem updateCartItemQuantity(Cart cart, CartItem cartItem, int newQuantity);

    Double getCartItemPrice(CartItem cartItem);

    void deleteAllCartItems(Cart cart);

    List<CartItem> getAllCartItems(Cart cart);

    int getCartItemsQuantity(Cart cart);

}
