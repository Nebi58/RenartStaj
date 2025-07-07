package tr.edu.duzce.mf.bm.renart.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.stereotype.Service;
import tr.edu.duzce.mf.bm.renart.model.Product;

import java.io.IOException;
import java.util.List;

@Service
public class LlmService {

    private static final String API_KEY = "AIzaSyC5OrI81dHL06kPoyMqHvytWdmo8w84AdA";
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;



    public String sendMessage(String userQuestion, List<Product> products) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(ENDPOINT); // Bir post isteği hazırladık
            httpPost.setHeader("Content-Type", "application/json");

            // Ürün listesini string'e çevir.Çünkü gemini sadece string'e cevap verebiliyor.
            StringBuilder productsInfo = new StringBuilder();
            for (Product product : products) {
                productsInfo.append(String.format("- %s (Popülerlik: %.1f/5, Fiyat: $%.2f USD, Ağırlık: %.1f gr)\n", 
                    product.getName(), 
                    product.getPopularityOutOf5(), 
                    product.getPrice(), 
                    product.getWeight()));
            }

            // Ürünler hakkında soru-cevap prompt'u
            String prompt = """
            Sen bir mücevher mağazasının akıllı asistanısın. Aşağıdaki ürün listesine göre kullanıcının sorusunu yanıtla:

            ## Mevcut Ürünler:
            %s

            ## Kullanıcı Sorusu:
            %s

            Lütfen:
            1. Kullanıcının sorusunu nazikçe yanıtla
            2. Ürünler hakkında doğru bilgi ver
            3. Gerekirse önerilerde bulun
            4. Türkçe yanıt ver
            5. Kısa ve öz ol (maksimum 3-4 cümle)
            """.formatted(productsInfo.toString(), userQuestion);

            ObjectMapper mapper = new ObjectMapper();
            String safeText = mapper.writeValueAsString(prompt);

            String json = """
            {
                "contents": [
                    {
                "parts": [
                    {
                    "text": %s
                    }
                ]
                    }
                ]
            }
            """.formatted(safeText);

            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            var response = client.execute(httpPost);  // İsteği yolladık ve gemini'Dan gelen cevabı aldık
            int statusCode = response.getCode();
            String responseBody = new String(response.getEntity().getContent().readAllBytes()); // gelen cevabı string'e çevirdik.

            if (statusCode != 200) {
                throw new IOException("Gemini API başarısız yanıt verdi. Status: " + statusCode + ", Body: " + responseBody);
            }

            JsonNode root = mapper.readTree(responseBody);
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                String result = candidates.get(0).path("content").path("parts").get(0).path("text").asText();

                // Basit post-processing: Gereksiz boş satırları azalt
                result = result.replaceAll("\\n{3,}", "\n\n").trim();

                return result;
            } else {
                return "Üzgünüm, şu anda yanıt veremiyorum. Lütfen daha sonra tekrar deneyin.";
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException("Gemini API çağrısında hata oluştu.", e);
        }
    }
}
