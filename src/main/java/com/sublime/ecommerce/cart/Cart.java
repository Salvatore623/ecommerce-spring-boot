package com.sublime.ecommerce.cart;

import com.sublime.ecommerce.security.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_cart")
public class Cart {

    @Id
    @GeneratedValue
    private Integer id;

    private Set<Integer> productsId;

    @OneToOne(orphanRemoval = true)
    private User user;

}
