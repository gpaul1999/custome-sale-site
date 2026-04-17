package com.customsalesite.repository;

import com.customsalesite.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByProductDetailId(Long productDetailId);
    List<Promotion> findByEnabled(boolean enabled);
}

