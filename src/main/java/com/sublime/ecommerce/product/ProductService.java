package com.sublime.ecommerce.product;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getProductById(Integer id){
        return productRepository.findById(id)
                .orElseThrow( () -> new EntityNotFoundException("Il prodotto non esiste"));
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        return productRepository.findAllByCategory(category);
    }

    public Map<ProductCategory, Long> countProductsByCategory() {
        var result = productRepository.countProductsByCategory();
        Map<ProductCategory, Long> countMap = new HashMap<>();
        for (Object[] row : result) {
            countMap.put((ProductCategory) row[0], (Long) row[1]);
        }
        return countMap;
    }

    public Set<Product> getProductsById(Set<Integer> idProducts) {
        Set<Product> productIdList = new HashSet<>();
        idProducts.forEach(id -> {
            var product = productRepository.findById(id)
                    .orElseThrow( () -> new EntityNotFoundException("Prodotto con id " + id + " non trovato"));
            productIdList.add(product);
        });
        return productIdList;
    }
}
