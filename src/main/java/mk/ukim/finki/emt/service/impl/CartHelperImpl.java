package mk.ukim.finki.emt.service.impl;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.Cart;
import mk.ukim.finki.emt.model.jpa.CartItem;
import mk.ukim.finki.emt.persistence.CartItemRepository;
import mk.ukim.finki.emt.persistence.CartRepository;
import mk.ukim.finki.emt.service.CartServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Bojan on 4/6/2017.
 */
@Service
public class CartHelperImpl implements CartServiceHelper {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    @Autowired
    public CartHelperImpl(CartRepository cartRepository, CartItemRepository cartItemRepository){
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Cart createCart(LocalDateTime expiryDate, Double totalPrice) {
        Cart cart = new Cart();
        cart.expiryDate = expiryDate;
        cart.totalPrice = totalPrice;
        return cartRepository.save(cart);
    }

    @Override
    public void removeCart(Long cartId) {
        cartRepository.delete(cartId);
    }

    @Override
    public CartItem addCartItem(Cart cart, Book book, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.cart = cart;
        cartItem.book = book;
        cartItem.quantity = quantity;
        addToTotalPrice(cart, book.price*quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void removeCartItem(Cart cart, CartItem cartItem) {
        subtractFromTotalPrice(cart, cartItem.book.price*cartItem.quantity);
        cartItemRepository.delete(cartItem.id);
    }

    @Override
    public Cart addToTotalPrice(Cart cart, Double price) {
        Double totalPrice = cart.totalPrice;
        cart.totalPrice = totalPrice + price;
        return cartRepository.save(cart);
    }

    @Override
    public Cart subtractFromTotalPrice(Cart cart, Double price) {
        Double totalPrice = cart.totalPrice;
        cart.totalPrice = totalPrice - price;
        return cartRepository.save(cart);
    }

    @Override
    public Double getCartItemPrice(Cart cart, CartItem cartItem) {
        return null;
    }

    @Override
    public void changeCartItemQuantity(Cart cart, CartItem cartItem) {

    }

    @Override
    public List<CartItem> getCartItems(Cart cart) {
        return null;
    }

    @Override
    public Long getCartTotal(Cart cart) {
        return null;
    }

    @Override
    public int getCartItemsQuantity(Cart cart) {
        return 0;
    }
}
