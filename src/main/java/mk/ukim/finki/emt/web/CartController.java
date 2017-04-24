package mk.ukim.finki.emt.web;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.Cart;
import mk.ukim.finki.emt.model.jpa.CartItem;
import mk.ukim.finki.emt.persistence.BookRepository;
import mk.ukim.finki.emt.persistence.CartItemRepository;
import mk.ukim.finki.emt.persistence.CartRepository;
import mk.ukim.finki.emt.service.CartServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import javax.servlet.http.*;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Enumeration;

/**
 * Created by Bojan on 4/20/2017.
 */
@Controller
public class CartController {

    @Autowired
    CartServiceHelper cartHelper;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    BookRepository bookRepository;

    @RequestMapping(value = {"/cart"}, method = RequestMethod.GET)
    public String cart(Model model,
                       HttpServletRequest request,
                       HttpServletResponse response){


        Cart cart;
        Cookie cartCookie = getCartCookie(request);

        if(cartCookie != null){
            //projdeno
            Long cartId = Long.parseLong(cartCookie.getValue());
            cart = cartRepository.findOne(cartId);
        }
        else{
            cart = cartHelper.createCart(LocalDateTime.now().plusWeeks(1), 0d);
            cartCookie = new Cookie("cart", cart.id.toString());
            cartCookie.setMaxAge(604800);
            cartCookie.setPath("/");
            response.addCookie(cartCookie);
        }

        model.addAttribute("cart", cart);
        model.addAttribute("quantity", cartHelper.getCartItemsQuantity(cart));
        model.addAttribute("cartHelper", cartHelper);
        model.addAttribute("pageFragment", "cart");
        return "index";
    }

    @RequestMapping(value = {"/cart/cartItem"}, method = RequestMethod.POST)
    public String cartItem(Model model,
                           @RequestParam String action,
                           @RequestParam String cartItemId,
                           @RequestParam String cartQuantity,
                           HttpServletRequest request){


        Cookie cartCookie = getCartCookie(request);
        Long cartId = Long.parseLong(cartCookie.getValue());


        Cart cart = cartRepository.findOne(cartId);
        CartItem cartItem = cartItemRepository.findOne(Long.parseLong(cartItemId));
        if( Integer.parseInt(action) == 1 ){
            //handle update
            //projdeno
            int x;
            cartHelper.updateCartItemQuantity(cart, cartItem, Integer.parseInt(cartQuantity));
        }
        else if( Integer.parseInt(action) == 2  ){
            //handle delete
            //projdeno
            int x;
            cartHelper.removeCartItem(cart, cartItem);
        }

        cart = cartRepository.findOne(cartId);
        model.addAttribute("cartItems", cartHelper.getAllCartItems(cart));
        model.addAttribute("totalPrice", cart.totalPrice);
        model.addAttribute("totalQuantity", cartHelper.getCartItemsQuantity(cart));
        model.addAttribute("pageFragment", "cart");
        return "redirect:/cart";
    }

    @RequestMapping(value = {"/cart/action"}, method = RequestMethod.POST)
    public String cartAction(Model model,
                             @RequestParam String backCheckout,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {


        Cookie cartCookie = getCartCookie(request);


        if( Integer.parseInt(backCheckout) == 1 ){
            //continue shopping
            int x;
            return "redirect:/";
        }
        else if( Integer.parseInt(backCheckout) == 2 ) {
            //checkout
            cartCookie.setPath("/");
            cartCookie.setValue(null);
            cartCookie.setMaxAge(0);
            response.addCookie(cartCookie);
            return "redirect:/";
        }
        else{
            //delete all cart items
            cartHelper.deleteAllCartItems(cartRepository.findOne(Long.parseLong(cartCookie.getValue())));
            return "redirect:/cart";
        }
    }


    @RequestMapping(value = {"/cart/addItem"}, method = RequestMethod.POST)
    public String addCartItem(@RequestParam String prodID,
                              @RequestParam String categoryId,
                              @RequestParam String quantity,
                              HttpServletRequest request,
                              HttpServletResponse response){

        Cookie cartCookie = getCartCookie(request);

        Cart cart;

        if(cartCookie != null){
            Long cartId = Long.parseLong(cartCookie.getValue());
            cart = cartRepository.findOne(cartId);

        }
        else{
            cart = cartHelper.createCart(LocalDateTime.now().plusWeeks(1), 0d);
            cartCookie = new Cookie("cart", cart.id.toString());
            cartCookie.setMaxAge(604800);
            cartCookie.setPath("/");
            response.addCookie(cartCookie);
        }

        Long idItem = Long.parseLong(prodID);
        Book book = bookRepository.findOne(idItem);

        cartHelper.addCartItem(cart, book, Integer.parseInt(quantity));
        return "redirect:/category/"+categoryId;
    }

    private Cookie getCartCookie(HttpServletRequest request){
        Cookie resultCookie = null;
        Cookie [] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie c: cookies) {
                if(c.getName().equals("cart")) {
                    return c;
                }
            }
        }
        return resultCookie;
    }

}
