package com.customsalesite.repository;

import com.customsalesite.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findByEnabled(boolean enabled);
    List<ProductCategory> findByProductTypeId(Long productTypeId);
    List<ProductCategory> findByProductTypeIdAndEnabled(Long productTypeId, boolean enabled);
}

