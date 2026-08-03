package com.kijoukoi.app.infrastructure;

import com.kijoukoi.app.domain.Blade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BladeRepository extends JpaRepository<Blade, Long> {
}
