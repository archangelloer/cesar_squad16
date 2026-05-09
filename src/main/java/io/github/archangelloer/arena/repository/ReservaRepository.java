package io.github.archangelloer.arena.repository;

import io.github.archangelloer.arena.model.Evento;
import io.github.archangelloer.arena.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    Reserva findByCodigoTexto(String codigoTexto);

    long countByEvento(Evento evento); // Conta total de reservas daquele evento
    long countByEventoAndUtilizado(Evento evento, boolean utilizado); // Conta só quem fez check-in (true) ou quem faltou (false)
}