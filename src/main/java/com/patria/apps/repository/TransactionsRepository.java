package com.patria.apps.repository;

import com.patria.apps.entity.Transactions;
import com.patria.apps.vo.StatusTransactionEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long>, JpaSpecificationExecutor<Transactions> {

    Optional<Transactions> findByIdAndDeletedAtIsNull(Long productId);

    Optional<Transactions> findByIdAndDeletedAtIsNotNull(Long id);

    List<Transactions> findByStatus(StatusTransactionEnum status);
}
