package com.sublime.ecommerce.security.admin;

import com.sublime.ecommerce.product.Product;
import com.sublime.ecommerce.product.ProductRepository;
import com.sublime.ecommerce.security.user.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sublime.ecommerce.security.user.Role.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public void createProduct(Product product) {
        productRepository.save(product);
    }


    public void changeRole(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Username non trovata"));
        if(user.getRole().name().equals(ROLE_ADMIN.name())){
            throw new EntityExistsException("Il ruolo di " + email + " è già admin");
        }
        user.setRole(ROLE_ADMIN);
        userRepository.save(user);
    }

}