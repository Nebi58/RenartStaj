package tr.edu.duzce.mf.bm.renart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tr.edu.duzce.mf.bm.renart.dao.ProductDAO;
import tr.edu.duzce.mf.bm.renart.model.Product;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    public List<Product> getAllProducts() {
        List<Product> products = productDAO.findAll();
        double goldPrice = getGoldPrice();

        // Her bir product okunuyır
        for(Product product : products) {
            // Verilen matematiksel formüle göre her bir product'ın hesabı yapıldı
            double productPrice = (product.getPopularityScore() + 1) * product.getWeight() * goldPrice;
            product.setPrice(Math.round(productPrice * 100.0) / 100.0);
            product.setPopularityOutOf5(Math.round(product.getPopularityScore() * 5 * 10.0) / 10.0);
        }
        return products;
    }



    // Bu fonksyion ile altın fiyatları güncel olarak internetten çekildi
    private double getGoldPrice() {
        try{
            RestTemplate restTemplate = new RestTemplate(); // http isteği atmak için kullanıyoruz.Aşağıda http isteği attık
            List<List<Object>> response = restTemplate.getForObject("https://api.metals.live/v1/spot", List.class);

            /* çektiğimiz verilerin gösterimi aşağıdaki gibidir:

                    [
                        ["gold", 2357.5],
                        ["silver", 30.1]
                    ]
            */

            for(List<Object> item : response) {
                if("gold".equalsIgnoreCase(item.get(0).toString())) {
                    return Double.parseDouble(item.get(1).toString());  // Eğer gold yazıyorsa parasını döndürüyor.
                }
            }
        }
        catch(Exception e) {
            System.out.println("Altın fiyatı alınamadı, varsayılan değer kullanılıyor...");
        }
        return 101.00; // Varsayılan değer olarak 101.00 USD
    }


}
