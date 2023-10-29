package com.sublime.ecommerce.order;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sublime.ecommerce.security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_order")
public class Order {

    @Id
    @GeneratedValue
    private Integer id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOrder;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateOrderDate;

    @ManyToOne
    @JsonBackReference
    private User user;

    private OrderStatus status;

    private BigDecimal totalPrice;

    @ElementCollection
    @CollectionTable(name = "order_details")
    private List<OrderDetails> orderDetails;

}
