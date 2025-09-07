package com.patria.apps.repository;

import com.patria.apps.entity.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    Optional<Payment> findByIdAndDeletedAtIsNull(Long productId);

    Optional<Payment> findByIdAndDeletedAtIsNotNull(Long id);

}
