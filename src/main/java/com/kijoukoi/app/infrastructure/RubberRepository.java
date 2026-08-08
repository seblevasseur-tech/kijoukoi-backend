package com.kijoukoi.app.infrastructure;

import com.kijoukoi.app.domain.Rubber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RubberRepository extends JpaRepository<Rubber, Long> {
    Optional<Rubber> findByNameIgnoreCase(String name);
}
