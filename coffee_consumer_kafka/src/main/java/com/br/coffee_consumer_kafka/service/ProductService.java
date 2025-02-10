package com.br.coffee_consumer_kafka.service;

import com.br.coffee_consumer_kafka.domain.ProductModel;
import com.br.coffee_consumer_kafka.repository.ProductRepository;
import com.br.coffee_consumer_kafka.service.exceptions.DatabaseException;
import com.br.coffee_consumer_kafka.service.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductModel> listAll(){
        return productRepository.findAll();
    }

    public ProductModel findById(Long id) {
        Optional<ProductModel> user = productRepository.findById(id);
        return user.orElseThrow(()-> new ResourceNotFoundException(id));
    }

    public ProductModel insert(ProductModel product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public ProductModel update(Long id, ProductModel user) {
        try {
            ProductModel obj = productRepository.getReferenceById(id);
            updateData(obj, user);
            return productRepository.save(obj);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(ProductModel entity, ProductModel obj) {
        entity.setName(Optional.ofNullable(obj.getName()).orElse(entity.getName()));
        entity.setDescription(Optional.ofNullable(obj.getDescription()).orElse(entity.getDescription()));
        entity.setPrice(Optional.ofNullable(obj.getPrice()).orElse(entity.getPrice()));
    }
}
