package com.patria.apps.repository;

import com.patria.apps.entity.BlackListToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Long>, JpaSpecificationExecutor<BlackListToken> {

    Optional<BlackListToken> findByTokenAndDeletedAtIsNull(String token);

    @Modifying
    @Transactional
    @Query("delete from BlackListToken where token = :token")
    void deleteByToken(String token);
}
