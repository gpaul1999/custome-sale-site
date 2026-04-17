package com.customsalesite.repository;

import com.customsalesite.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
    List<ProductType> findByEnabled(boolean enabled);
}

