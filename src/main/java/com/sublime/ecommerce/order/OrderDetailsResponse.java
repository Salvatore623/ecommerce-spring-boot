package com.sublime.ecommerce.order;

import com.sublime.ecommerce.product.ProductCategory;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderDetailsResponse(
        Integer id,
        String name,
        BigDecimal price,
        ProductCategory category,
        String img,
        Integer quantity
) {
}
