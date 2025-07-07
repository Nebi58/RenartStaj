package tr.edu.duzce.mf.bm.renart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    private String name;
    private double popularityScore;
    private double weight;
    private ProductImage images;

    private double price;  // Hesaplanmış değer
    private double popularityOutOf5; // Ekstra gösterim için
}

// Bu model ile ürünün özelliklerini tanımlamış olduk
