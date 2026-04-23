package io.github.archangelloer.arena.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.archangelloer.arena.model.Evento;
import io.github.archangelloer.arena.model.Reserva;
import io.github.archangelloer.arena.repository.EventoRepository;
import io.github.archangelloer.arena.repository.ReservaRepository;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository repository;

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/")
    public String listarEventos(Model model){
        List<Evento> minhaLista = repository.findAll();

        model.addAttribute("eventos", minhaLista);

        return "index";
    }

    @GetMapping("/novo-evento")
    public String exibirFormularioCadastro(Model model){
        model.addAttribute("novoEvento", new Evento());

        return "cadastro";
    }

    @PostMapping("/salvar-evento")
    public String salvarEvento(Evento eventoRecebido, Model model) {
        if (eventoRecebido.getData() != null && eventoRecebido.getData().isBefore(LocalDateTime.now())) {
            model.addAttribute("erroData", "A data do evento não pode ser no passado!");
            model.addAttribute("novoEvento", eventoRecebido); 
            return "cadastro";
        }
        repository.save(eventoRecebido);
        return "redirect:/";
    }

    @GetMapping("/eventos/{id}/reservar")
    public String realizarReserva(@PathVariable Long id, Model model) {
        Evento evento = repository.findById(id).orElse(null);

        if (evento != null && evento.getCapacidadeDisponivel() > 0) {
            evento.reservarIngresso();
            repository.save(evento);

            Reserva novaReserva = new Reserva();
            novaReserva.setEvento(evento);
            novaReserva.setNome("Usuário Simulado");
            novaReserva.setCodigoTexto("ARENA-" + System.currentTimeMillis());
            novaReserva.setUtilizado(false);
            
            reservaRepository.save(novaReserva);    

            model.addAttribute("nomeEvento", evento.getNome());
            model.addAttribute("codigo", novaReserva.getCodigoTexto());

            return "sucesso";
        }
        return "redirect:/"; 
    }
    
    @GetMapping("/validacao")
    public String exibirTelaValidacao() {
        return "validacao"; 
    }

    @PostMapping("/validar-ingresso")
    public String validarIngresso(String codigo, Model model){
        Reserva reservaEncontrada = reservaRepository.findByCodigoTexto(codigo);

        if (reservaEncontrada == null) {
            model.addAttribute("mensagemErro", "❌ Ingresso não encontrado. Verifique o código.");
            return "validacao"; 
        }

        if (reservaEncontrada.isUtilizado()) {
            model.addAttribute("mensagemErro", "⚠️ Este ingresso já foi utilizado para check-in!");
            return "validacao";
        }

        reservaEncontrada.setUtilizado(true); // Muda o status para não usar de novo
        reservaRepository.save(reservaEncontrada);

        model.addAttribute("mensagemSucesso", "✅ Ingresso validado! Catraca liberada para " + reservaEncontrada.getNome());
        return "validacao";
    }
}
