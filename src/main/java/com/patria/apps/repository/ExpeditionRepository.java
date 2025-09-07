package com.patria.apps.repository;

import com.patria.apps.entity.Expedition;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpeditionRepository extends JpaRepository<Expedition, Long>, JpaSpecificationExecutor<Expedition> {

    Optional<Expedition> findByIdAndDeletedAtIsNull(Long productId);

    Optional<Expedition> findByIdAndDeletedAtIsNotNull(Long id);

}
