package com.sublime.ecommerce.order;

import jakarta.persistence.Embeddable;

@Embeddable
public record OrderDetails(Integer productId, Integer quantity) {
}
