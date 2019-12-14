package com.software.nick.shop.controller;

import com.software.nick.shop.model.Product;
import com.software.nick.shop.model.User;
import com.software.nick.shop.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private ProductRepo productRepo;

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

}
