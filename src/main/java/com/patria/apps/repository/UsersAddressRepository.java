package com.patria.apps.repository;

import com.patria.apps.entity.UsersAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersAddressRepository extends JpaRepository<UsersAddress, Long>, JpaSpecificationExecutor<UsersAddress> {

}
