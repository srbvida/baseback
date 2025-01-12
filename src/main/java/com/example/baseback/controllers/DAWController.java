package com.example.baseback.controllers;

import com.example.baseback.repository.ProductRepository;
import com.example.baseback.service.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping("/api")
@SuppressWarnings({"java:S5738", "java:S4684"})
class DAWController {
    private ProductRepository repository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.debug("A request has arrived to login");
        if ("user".equals(request.getUsername()) && "password".equals(request.getPassword())) {
            return ResponseEntity.ok(new LoginResponse("Bearer dummy-token"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @GetMapping(value = "/products")
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        log.debug("A request has arrived to get all Products");
        return ResponseEntity.ok(repository.findAll());

    }

    @PostMapping("/product")
    public ProductEntity createProduct(@RequestBody ProductEntity product) {
        return repository.save(product);
    }

    @PutMapping("/product")
    public ProductEntity updateProduct(@RequestBody ProductEntity person) {
        return repository.save(person);
    }

    @GetMapping(value = "/products/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable Long id) {
        log.debug("A request has arrived to get product by id: {} ", id);
        Optional<ProductEntity> productOptional = repository.findById(id);

        // Verificar si el producto está presente
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.get()); // Devolver el producto encontrado con estado 200 OK
        } else {
            log.error("Product with id {} not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Devolver 404 si no se encuentra el producto
        }
    }

    @DeleteMapping("/product/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("id") Long id) {
        log.info("A request has been received to delete the product with id {}", id);
        repository.deleteById(id);
    }
}

