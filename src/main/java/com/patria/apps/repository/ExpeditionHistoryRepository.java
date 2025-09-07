package com.patria.apps.repository;

import com.patria.apps.entity.ExpeditionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpeditionHistoryRepository extends JpaRepository<ExpeditionHistory, Long>, JpaSpecificationExecutor<ExpeditionHistory> {

}
