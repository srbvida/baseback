package com.example.baseback.controllers;

import com.example.baseback.repository.ProductRepository;
import com.example.baseback.security.JwtUtil;
import com.example.baseback.security.UserEntity;
import com.example.baseback.security.UserService;
import com.example.baseback.service.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.debug("A request has arrived to login");
        try{
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            // Validar la contraseña
            if (userService.validatePassword(request.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(userDetails.getUsername())));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos");
            }
        }catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

    }

    @PostMapping("/user")
    public void createUser(@RequestBody LoginRequest request) {
        log.debug("A request has arrived to create user");
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        userService.createUser(user);

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

