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

    @GetMapping("/{userId}")
    public List<Cart> getCart(
            @PathVariable Integer userId){

        return cartService.getCart(userId);

    }

    @PostMapping
    public String addToCart(@RequestBody Cart cart){

        cartService.addToCart(cart);

        return "Added to cart.";

    }

    @DeleteMapping("/{cartId}")
    public String removeItem(
            @PathVariable Integer cartId){

        cartService.removeItem(cartId);

        return "Removed.";

    }

    @PutMapping("/increase/{cartId}")
    public String increase(@PathVariable Integer cartId){

        cartService.increaseQuantity(cartId);

        return "Quantity increased.";

    }

    @PutMapping("/decrease/{cartId}")
    public String decrease(@PathVariable Integer cartId){

        cartService.decreaseQuantity(cartId);

        return "Quantity decreased.";

    }

    @DeleteMapping("/clear/{userId}")
    public String clearCart(
            @PathVariable Integer userId){

        cartService.clearCart(userId);

        return "Cart cleared.";

    }

}