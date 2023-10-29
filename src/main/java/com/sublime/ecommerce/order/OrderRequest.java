package com.sublime.ecommerce.order;

import lombok.Builder;


import java.util.List;

@Builder
public record OrderRequest(
        Integer id,

        double totalPrice,

        List<OrderDetails> orderDetails

) {
}
