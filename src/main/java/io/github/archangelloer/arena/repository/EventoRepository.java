package io.github.archangelloer.arena.repository;

import io.github.archangelloer.arena.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
}