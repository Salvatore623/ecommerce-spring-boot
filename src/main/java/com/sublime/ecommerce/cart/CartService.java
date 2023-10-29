package com.sublime.ecommerce.cart;

import com.sublime.ecommerce.product.Product;
import com.sublime.ecommerce.product.ProductRepository;
import com.sublime.ecommerce.security.user.User;
import com.sublime.ecommerce.security.user.UserRepository;
import com.sublime.ecommerce.security.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserService userService;

    public Integer addListProductToCart(Set<Integer> productsId){
        Set<Integer> productIdList = new HashSet<>();
        productsId.forEach(id -> {
        var product = productRepository.findById(id)
                    .orElseThrow( () -> new EntityNotFoundException("Prodotto con id " + id + " non trovato"));
            productIdList.add(product.getId());
        });
        var user = userService.extractUserFromToken();
        if(user.getCart() != null){
            productsId.forEach(id -> {
                productRepository.findById(id)
                        .orElseThrow( () -> new EntityNotFoundException("Prodotto con id " + id + " non trovato"));
                user.getCart().getProductsId().add(id);
            });
            userRepository.save(user);
        } else {
            cartRepository.save(newCart(user, productIdList));
        }
        productIdList.clear();
        return user.getCart().getProductsId().size();
    }


    public Integer addOneProductToCart(Integer id){
        var user = userService.extractUserFromToken();
        if(user.getCart() != null){
            productRepository.findById(id)
                    .orElseThrow( () -> new EntityNotFoundException("Prodotto con id " + id + " non trovato"));
            user.getCart().getProductsId().add(id);
            userRepository.save(user);
        } else {
            cartRepository.save(newCart(user, Collections.singleton(id)));
        }
        return user.getCart().getProductsId().size();
    }

    private Cart newCart(User user, Set<Integer> productIdList){
        return Cart.builder()
                .user(user)
                .productsId(productIdList)
                .build();
    }

    public Set<Product> getCart(){
        Set<Product> productList = new HashSet<>();
        var user = userService.extractUserFromToken();
        if(user.getCart() == null){
            return productList;
        }
        return productsListUpdated(productList, user);
    }

    public Set<Product> RemoveProductFromCart(Integer id){
        Set<Product> productList = new HashSet<>();
        var user = userService.extractUserFromToken();
        var productsInCart = user.getCart().getProductsId();
            if (productsInCart.contains(id)) {
                user.getCart().getProductsId().remove(id);
            }
        userRepository.save(user);
        return productsListUpdated(productList, user);
    }


    private Set<Product> productsListUpdated(Set<Product> productList, User user){
        user.getCart().getProductsId().forEach(id -> {
            var product = productRepository.findById(id)
                    .orElseThrow( () -> new EntityNotFoundException("Prodotto con id " + id + " non trovato"));
            productList.add(product);
        });
        return productList;
    }

    public Integer getUserCartSize(){
        var user = userService.extractUserFromToken();
        if(user.getCart() != null){
            return user.getCart().getProductsId().size();
        } else {
            return 0;
        }
    }

    public void clearCart(){
        var user = userService.extractUserFromToken();
        user.getCart().getProductsId().clear();
        userRepository.save(user);
    }


}
