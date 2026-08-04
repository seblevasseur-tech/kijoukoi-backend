package com.kijoukoi.app.infrastructure;

import com.kijoukoi.app.domain.RubberType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RubberTypeRepository extends JpaRepository<RubberType, Long> {
}
