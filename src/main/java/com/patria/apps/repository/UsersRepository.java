package com.patria.apps.repository;

import com.patria.apps.entity.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {
    
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);
    
    Optional<Users> findByIdAndDeletedAtIsNull(Long id);

    Optional<Users> findByIdAndDeletedAtIsNotNull(Long id);
    
    List<Users> findByRole_RoleName(String roleName);
}
