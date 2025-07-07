package tr.edu.duzce.mf.bm.renart.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.renart.model.Product;
import tr.edu.duzce.mf.bm.renart.model.ProductImage;

import java.io.InputStream;
import java.util.List;

@Repository
public class ProductDAO {

    public List<Product> findAll() {

        try{
            ObjectMapper mapper = new ObjectMapper(); // Json dosyasındaki verileri java nesnesine dönüştürmeyi sağlıyor.
            // Yukarıdaki kodla Her bir verisi  Product nesnesine dönüşecek.
            InputStream in = getClass().getClassLoader().getResourceAsStream("products.json");
            return mapper.readValue(in, new TypeReference<List<Product>>() {}); // json nesnesinde okuduğu verileri List<Product>'a dönüştürür
        }
        catch(Exception e){
            e.printStackTrace();
            return List.of();
        }
    }

}
