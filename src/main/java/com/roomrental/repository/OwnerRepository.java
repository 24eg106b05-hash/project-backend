package com.roomrental.repository;

import com.roomrental.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    boolean existsByEmailIgnoreCase(String email);
}
