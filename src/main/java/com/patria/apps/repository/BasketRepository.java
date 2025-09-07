package com.patria.apps.repository;

import com.patria.apps.entity.Basket;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long>, JpaSpecificationExecutor<Basket> {
 
    Optional<Basket> findByUsersIdAndProductId(Long userId, Long productId);
}
