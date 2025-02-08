package com.misanimes.animefavoritos.repository;

import com.misanimes.animefavoritos.entity.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    Optional<Anime> findByIdAndUserId(Long id, Long userId);
}


