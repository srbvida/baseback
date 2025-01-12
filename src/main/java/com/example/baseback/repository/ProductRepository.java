package com.example.baseback.repository;

import com.example.baseback.service.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>{

    List<ProductEntity> findByName(@Param("name") String name);

}
