package com.kijoukoi.app.infrastructure;

import com.kijoukoi.app.domain.BladeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BladeTypeRepository extends JpaRepository<BladeType, Long> {
}
