package com.example.floranest.backend.controller;

import com.example.floranest.backend.entity.Cart;
import com.example.floranest.backend.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<Cart> getAllCartItems() {
        return cartService.getAllCartItems();
    }

    @GetMapping("/{id}")
    public Cart getCartItemById(@PathVariable Integer id) {
        return cartService.getCartItemById(id);
    }

    @PostMapping
    public String addCartItem(@RequestBody Cart cart) {
        cartService.addCartItem(cart);
        return "Cart item added successfully.";
    }

    @PutMapping("/{id}")
    public String updateCartItem(@PathVariable Integer id,
                                 @RequestBody Cart cart) {
        cart.setCartId(id);
        cartService.updateCartItem(cart);
        return "Cart item updated successfully.";
    }

    @DeleteMapping("/{id}")
    public String deleteCartItem(@PathVariable Integer id) {
        cartService.deleteCartItem(id);
        return "Cart item deleted successfully.";
    }

}