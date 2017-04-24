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
        deleteAllCartItems(cartRepository.findOne(cartId));
        cartRepository.delete(cartId);
    }

    @Override
    public CartItem addCartItem(Cart cart, Book book, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.cart = cart;
        cartItem.book = book;
        cartItem.quantity = quantity;

        //ako novo dodadenata kniga e duplikat, ja brise starta, ja stava novata
        CartItem existing = cartItemRepository.findBookInCart(cart.id, book.id);
        if(existing != null){
            removeCartItem(cart, existing);
        }

        Double totalPrice = cart.totalPrice;
        totalPrice += getCartItemPrice(cartItem);
        updateCartTotalPrice(cart, totalPrice);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void removeCartItem(Cart cart, CartItem cartItem) {
        Double totalPrice = cart.totalPrice;
        totalPrice -= getCartItemPrice(cartItem);
        updateCartTotalPrice(cart, totalPrice);
        cartItemRepository.delete(cartItem.id);
    }

    @Override
    public Cart updateCartTotalPrice(Cart cart, Double price) {
        cart.totalPrice = price;
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateCartExpiryDate(Cart cart, LocalDateTime newExpiryDate) {
        cart.expiryDate = newExpiryDate;
        return cartRepository.save(cart);
    }

    @Override
    public CartItem updateCartItemQuantity(Cart cart, CartItem cartItem, int newQuantity) {
        Double totalPrice = cart.totalPrice;
        cart.totalPrice = totalPrice - getCartItemPrice(cartItem);

        cartItem.quantity = newQuantity;

        cart.totalPrice += getCartItemPrice(cartItem);
        cartRepository.save(cart);

        return cartItemRepository.save(cartItem);
    }

    @Override
    public Double getCartItemPrice(CartItem cartItem) {
        return cartItem.quantity * cartItem.book.price;
    }

    @Override
    public void deleteAllCartItems(Cart cart) {
        List<CartItem> cartItemList = getAllCartItems(cart);

        for (CartItem ca:cartItemList ) {
            cartItemRepository.delete(ca.id);
        }

        cart.totalPrice = 0d;
        cartRepository.save(cart);

    }

    @Override
    public List<CartItem> getAllCartItems(Cart cart) {
        return cartItemRepository.findByCartId(cart.id);
    }

    @Override
    public int getCartItemsQuantity(Cart cart) {
        int totalCartItemsQuantity = 0;

        List<CartItem> cartItems = getAllCartItems(cart);
        for (CartItem ca:cartItems) {
            totalCartItemsQuantity += ca.quantity;
        }
        return totalCartItemsQuantity;
    }

}
