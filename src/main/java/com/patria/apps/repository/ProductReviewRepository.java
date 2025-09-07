package com.patria.apps.repository;

import com.patria.apps.entity.ProductReview;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>, JpaSpecificationExecutor<ProductReview> {

    Optional<ProductReview> findByIdAndDeletedAtIsNull(Long productId);

    Optional<ProductReview> findByIdAndDeletedAtIsNotNull(Long id);

    Optional<ProductReview> findByUsersIdAndProductId(Long userId, Long productId);

}
