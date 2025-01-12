package com.example.baseback.service;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String description;


    private Double price;
    private int stock;

    public ProductEntity() {

    }

    public ProductEntity(Long id, String name, String description, Double price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock= stock;
    }


    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", descripcion=" + description + ", precio=" + price + ", stock=" + stock + "]";
    }

}