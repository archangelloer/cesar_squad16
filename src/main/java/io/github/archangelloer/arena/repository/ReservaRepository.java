package io.github.archangelloer.arena.repository;

import io.github.archangelloer.arena.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    Reserva findByCodigoTexto(String codigoTexto);
}