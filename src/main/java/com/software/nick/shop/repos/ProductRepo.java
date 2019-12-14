package com.software.nick.shop.repos;

import com.software.nick.shop.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepo extends CrudRepository<Product, Long> {

    List<Product> findByTitle(String title);

}
