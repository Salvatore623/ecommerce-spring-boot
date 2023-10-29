package com.sublime.ecommerce.cart;

import com.sublime.ecommerce.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/cart")
@CrossOrigin("${allowed.origins}")
public class CartController {

    @Value("${allowed.origins}")
    private String allowedOrigins;

    private final CartService cartService;

    @GetMapping
    ResponseEntity<Set<Product>> cart(){
        return ResponseEntity.ok( cartService.getCart() );
    }

    @PostMapping("/one")
    ResponseEntity<Integer> addOneProductToCart(@RequestBody Integer id){
        return ResponseEntity.ok( cartService.addOneProductToCart(id));
    }

    @PostMapping("/list")
    ResponseEntity<Integer> addListProductToCart(@RequestBody Set<Integer> productsId){
        return ResponseEntity.ok( cartService.addListProductToCart(productsId));
    }

    @GetMapping("/size")
    ResponseEntity<Integer> userCartSize(){
        return ResponseEntity.ok( cartService.getUserCartSize() );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Set<Product>> delete(@PathVariable Integer id){
        return ResponseEntity.ok( cartService.RemoveProductFromCart(id) );
    }

    @GetMapping("/clear")
    ResponseEntity<Void> clearCart(){
        cartService.clearCart();
        return ResponseEntity.ok().build();
    }
}
