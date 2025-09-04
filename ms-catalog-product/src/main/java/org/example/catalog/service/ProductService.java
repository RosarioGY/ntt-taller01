package org.example.catalog.service;

import lombok.RequiredArgsConstructor;
import org.example.catalog.entity.Product;
import org.example.catalog.exception.InsufficientStockException;
import org.example.catalog.exception.NotFoundException;
import org.example.catalog.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public Product update(String id, String name, BigDecimal price) {
        Product product = findById(id);
        product.setName(name);
        product.setPrice(price);
        return productRepository.save(product);
    }

    public Product addStock(String id, Integer amount) {
        Product product = findById(id);
        product.setStock(product.getStock() + amount);
        return productRepository.save(product);
    }

    public Product purchase(String id, Integer quantity) {
        Product product = findById(id);

        if (product.getStock() < quantity) {
            throw new InsufficientStockException(id, quantity, product.getStock());
        }

        product.setStock(product.getStock() - quantity);
        return productRepository.save(product);
    }

    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
