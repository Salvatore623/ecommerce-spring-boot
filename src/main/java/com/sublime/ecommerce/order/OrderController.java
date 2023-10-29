package com.sublime.ecommerce.order;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
@CrossOrigin("${allowed.origins}")
public class OrderController {

    @Value("${allowed.origins}")
    private String allowedOrigins;

    private final OrderService orderService;

    @PostMapping
    ResponseEntity<Void> createOrder(@RequestBody OrderRequest dto){
        orderService.createOrder(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<List<OrderResponse>> orderByUser(){
        return ResponseEntity.ok( orderService.getOrdersByUser() );
    }

    @DeleteMapping("/{id}")
    ResponseEntity<List<OrderResponse>> deleteOrderById(@PathVariable Integer id){
        return ResponseEntity.ok( orderService.deleteOrderById(id) );
    }

}
