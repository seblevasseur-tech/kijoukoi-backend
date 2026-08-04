package com.kijoukoi.app.infrastructure;

import com.kijoukoi.app.domain.Player;
import com.kijoukoi.app.domain.dto.BrandStatDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByLogin(String login);

    @Query("SELECT new com.kijoukoi.app.domain.dto.BrandStatDTO(b.name, COUNT(p)) " +
           "FROM Player p " +
           "JOIN p.racket.blade.brand b " +
           "WHERE p.ranking BETWEEN :minPoints AND :maxPoints " +
           "GROUP BY b.name " +
           "ORDER BY COUNT(p) DESC")
    List<BrandStatDTO> countBladeBrandsByRankingRange(@Param("minPoints") int minPoints, @Param("maxPoints") int maxPoints);
}
