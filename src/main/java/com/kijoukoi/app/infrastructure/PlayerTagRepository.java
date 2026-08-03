package com.kijoukoi.app.infrastructure;

import com.kijoukoi.app.domain.PlayerTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerTagRepository extends JpaRepository<PlayerTag, Long> {
    Optional<PlayerTag> findByName(String name);
}
