package com.sublime.ecommerce.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("api/v1/auth/products")
@RequiredArgsConstructor
@CrossOrigin("${allowed.origins}")
public class ProductController {

    private final ProductService productService;

    @Value("${allowed.origins}")
    private String allowedOrigins;

    @PostMapping
    ResponseEntity<Set<Product>> getProductsById(@RequestBody Set<Integer> idProducts) {
        return ResponseEntity.ok(  productService.getProductsById(idProducts) );
    }

    @GetMapping("/category/{category}")
    ResponseEntity<List<Product>> getProductsByCategory(@PathVariable ProductCategory category) {
        return ResponseEntity.ok(  productService.getProductsByCategory(category) );
    }

    @GetMapping("/{id}")
    ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(  productService.getProductById(id) );
    }

    @GetMapping("/category/count")
    ResponseEntity<Map<ProductCategory, Long>> countProductsByCategory() {
        return ResponseEntity.ok(  productService.countProductsByCategory() );
    }

}
