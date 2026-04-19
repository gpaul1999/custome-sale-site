package com.customsalesite.repository;

import com.customsalesite.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByProductCategoryId(Long productCategoryId);
    List<Product> findByProductCategoryIdAndEnabled(Long productCategoryId, boolean enabled);
    List<Product> findBySaleOff(boolean saleOff);
    List<Product> findByEnabled(boolean enabled);

    @Query("select p from Product p where p.enabled = true and (lower(p.syntax) like lower(concat('%', :q, '%')) or lower(p.description) like lower(concat('%', :q, '%'))) ")
    Page<Product> searchByKeyword(@Param("q") String q, Pageable pageable);
}
