package tr.edu.duzce.mf.bm.renart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tr.edu.duzce.mf.bm.renart.model.Product;
import tr.edu.duzce.mf.bm.renart.model.ProductImage;
import tr.edu.duzce.mf.bm.renart.service.ProductService;
import tr.edu.duzce.mf.bm.renart.service.LlmService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private LlmService llmService;

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String showProducts(
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "minPopularity", required = false) Double minPopularity,
            @RequestParam(value = "maxPopularity", required = false) Double maxPopularity,
            Model model) {
        List<Product> products = productService.getAllProducts();
        // Filtreleme
        if (minPrice != null) {
            products = products.stream().filter(p -> p.getPrice() >= minPrice).toList();
        }
        if (maxPrice != null) {
            products = products.stream().filter(p -> p.getPrice() <= maxPrice).toList();
        }
        if (minPopularity != null) {
            products = products.stream().filter(p -> p.getPopularityOutOf5() >= minPopularity).toList();
        }
        if (maxPopularity != null) {
            products = products.stream().filter(p -> p.getPopularityOutOf5() <= maxPopularity).toList();
        }
        model.addAttribute("products", products);
        // Filtre değerlerini JSP'ye tekrar gönder
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("minPopularity", minPopularity);
        model.addAttribute("maxPopularity", maxPopularity);
        return "products";
    }
    
    @PostMapping("/ask-llm")
    public String askLlm(@RequestParam String question, Model model) {
        try {
            List<Product> products = productService.getAllProducts();
            String answer = llmService.sendMessage(question, products);
            
            model.addAttribute("products", products);
            model.addAttribute("llmQuestion", question);
            model.addAttribute("llmAnswer", answer);
        } catch (Exception e) {
            model.addAttribute("llmError", "Üzgünüm, şu anda yanıt veremiyorum. Lütfen daha sonra tekrar deneyin.");
        }
        return "products";
    }
}
