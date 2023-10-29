package com.sublime.ecommerce.order;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Builder
public record OrderResponse(
        Integer id,
        Date dateOrder,
        OrderStatus status,
        Date updateOrderDate,
        BigDecimal totalPrice,
        List<OrderDetailsResponse> orderDetails
) {
}
