package com.blibli.training.product.service.impl;

import com.blibli.training.product.dto.ProductRequest;
import com.blibli.training.product.dto.ProductResponse;
import com.blibli.training.product.entity.Product;
import com.blibli.training.product.repository.ProductRepository;
import com.blibli.training.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .productName(request.getProductName())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        productRepository.save(product);
        return mapToResponse(product);
    }

    @Override
    public ProductResponse findByName(String name) {
        Product product = productRepository.findByProductName(name)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<ProductResponse> searchProduct(String query, int page, int size) {
        return productRepository.findByProductNameContainingIgnoreCase(query, PageRequest.of(page, size)).stream()
                .map(this::mapToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
