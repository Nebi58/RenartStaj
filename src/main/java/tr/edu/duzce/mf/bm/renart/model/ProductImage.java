package tr.edu.duzce.mf.bm.renart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductImage {

    private String yellow;
    private String rose;
    private String white;
}


// Product'ta kullanılacak olan resimleri alıyoruz burada
