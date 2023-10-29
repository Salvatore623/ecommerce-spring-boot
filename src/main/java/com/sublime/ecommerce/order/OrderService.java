package com.sublime.ecommerce.order;

import com.sublime.ecommerce.exception.InvalidTotalPriceException;
import com.sublime.ecommerce.product.Product;
import com.sublime.ecommerce.product.ProductRepository;
import com.sublime.ecommerce.security.user.UserRepository;
import com.sublime.ecommerce.security.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.sublime.ecommerce.order.OrderStatus.ATTIVO;
import static com.sublime.ecommerce.order.OrderStatus.CANCELLATO;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private double totalPrice = 0;

    public void createOrder(OrderRequest dto){
        var user = userService.extractUserFromToken();
        checkTotalPriceCorrected(dto);
        var order = Order.builder()
                .user(user)
                .orderDetails(dto.orderDetails())
                .status(ATTIVO)
                .totalPrice(BigDecimal.valueOf(dto.totalPrice()))
                .build();
        orderRepository.save(order);
    }

    public List<OrderResponse> getOrdersByUser(){
        List<OrderResponse> orderResponseList = new ArrayList<>();
        var user = userService.extractUserFromToken();

        var orders = orderRepository.findAllByUserId(user.getId());
        orders.forEach(order -> {
            List<OrderDetailsResponse> orderDetailsResponseList = new ArrayList<>();
            order.getOrderDetails().forEach(orderdetails -> {
                var product = productRepository.findById(orderdetails.productId())
                         .orElseThrow( () -> new EntityNotFoundException("Il prodotto non esiste") );
                orderDetailsResponseList.add(newOrderDetailsResponse(orderdetails, product));
            });
            orderResponseList.add(newOrderResponse(order, orderDetailsResponseList));
        });
        return orderResponseList;
    }

    private OrderDetailsResponse newOrderDetailsResponse(OrderDetails orderdetails, Product product){
        return OrderDetailsResponse.builder()
                .id(orderdetails.productId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .img(product.getImg())
                .quantity(orderdetails.quantity())
                .build();
    }

    private OrderResponse newOrderResponse(Order order, List<OrderDetailsResponse> orderDetailsResponseList){
        return OrderResponse.builder()
                .id(order.getId())
                .dateOrder(order.getDateOrder())
                .updateOrderDate(order.getUpdateOrderDate())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .orderDetails(orderDetailsResponseList)
                .build();
    }

    private void checkTotalPriceCorrected(OrderRequest dto){
        dto.orderDetails().forEach(order -> {
            var product = productRepository.findById(order.productId())
                    .orElseThrow( () -> new EntityNotFoundException("Il prodotto non esiste") );
            totalPrice += product.getPrice().doubleValue() * order.quantity();
        });
        if(dto.totalPrice() != totalPrice){
            totalPrice = 0;
            throw new InvalidTotalPriceException("Il prezzo è sbagliato");
        }
        totalPrice = 0;
    }


    public List<OrderResponse> deleteOrderById(Integer id) {
        var user = userService.extractUserFromToken();
        var order = orderRepository.findById(id)
                        .orElseThrow( () -> new EntityNotFoundException("L'ordine non esiste") );

        if(!user.getId().equals(order.getUser().getId())){
            throw new AccessDeniedException("Non hai i permessi per cancellare l'ordine");
        }
        order.setStatus(CANCELLATO);
        orderRepository.save(order);
        return getOrdersByUser();
    }




}
