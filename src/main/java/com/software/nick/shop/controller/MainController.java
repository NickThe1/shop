package com.software.nick.shop.controller;

import com.itextpdf.text.DocumentException;
import com.software.nick.shop.Service.PDFService;
import com.software.nick.shop.model.Product;
import com.software.nick.shop.model.User;
import com.software.nick.shop.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Controller
public class MainController {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private PDFService pdfService;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        Iterable<Product> products = productRepo.findAll();

        model.put("products", products);

        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @RequestParam String title, @RequestParam String description, @RequestParam String priceStr, Map<String, Object> model) {
        Integer price = null;

        try
        {
            price = Integer.parseInt(priceStr.trim());
        }
        catch (NumberFormatException nfe)
        {
            System.out.println("NumberFormatException: " + nfe.getMessage());
        }

        Product message = new Product(title, description, price, user);

        productRepo.save(message);

        Iterable<Product> products = productRepo.findAll();

        model.put("products", products);

        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<Product> products;

        if (filter != null && !filter.isEmpty()) {
            products = productRepo.findByTitle(filter);
        } else {
            products = productRepo.findAll();
        }

        model.put("products", products);

        return "main";
    }

    @GetMapping("/stats")
    public String getStats(Model model){

        List<Product> productList = productRepo.findAll();
        model.addAttribute("post", "test1");
        model.addAttribute("amount", productRepo.count());
        model.addAttribute("price", productList.stream().min(Comparator.comparingInt(Product::getPrice)).get().getPrice());
        model.addAttribute("expensive", productList.stream().max(Comparator.comparingInt(Product::getPrice)).get().getPrice());
        return "stats";
    }


    @Scheduled(cron = "0/15 * * * * *")
    @GetMapping("report")
    public void report() throws FileNotFoundException, DocumentException {
        pdfService.generateReport();
        System.out.println("---------------------Report----------------------------");
    }

}
