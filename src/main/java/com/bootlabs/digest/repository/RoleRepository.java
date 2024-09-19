package com.bootlabs.digest.repository;

import com.bootlabs.digest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
