package io.github.archangelloer.arena.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.archangelloer.arena.model.Evento;
import io.github.archangelloer.arena.model.Reserva;
import io.github.archangelloer.arena.repository.EventoRepository;
import io.github.archangelloer.arena.repository.ReservaRepository;
import io.github.archangelloer.arena.model.RelatorioDTO;

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
    public String validarIngresso(@RequestParam("codigo") String codigo, Model model){
        Reserva reservaEncontrada = reservaRepository.findByCodigoTexto(codigo);

        if (reservaEncontrada == null) {
            model.addAttribute("mensagemErro", "❌ Ingresso não encontrado. Verifique o código.");
            return "validacao"; 
        }

        if (reservaEncontrada.isUtilizado()) {
            model.addAttribute("mensagemErro", "⚠️ Este ingresso já foi utilizado para check-in!");
            return "validacao";
        }

        reservaEncontrada.setUtilizado(true);
        reservaRepository.save(reservaEncontrada);

        model.addAttribute("mensagemSucesso", "✅ Ingresso validado! Catraca liberada para " + reservaEncontrada.getNome());
        return "validacao";
    }

    @GetMapping("/relatorios")
    public String exibirRelatorio(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {
        
        List<Evento> eventosFiltrados;

        if (dataInicio != null && dataFim != null) {
            LocalDateTime inicio = dataInicio.atStartOfDay(); 
            LocalDateTime fim = dataFim.atTime(23, 59, 59);
            
            eventosFiltrados = repository.findByDataBetween(inicio, fim);
        } else {
            eventosFiltrados = repository.findAll();
        }

        List<RelatorioDTO> dadosRelatorio = new ArrayList<>();

        for (Evento evento : eventosFiltrados) {
            
            long reservasAtivas = reservaRepository.findAll().stream()
                    .filter(r -> r.getEvento() != null 
                              && r.getEvento().getId().equals(evento.getId()) 
                              && !"Cancelado".equals(r.getStatus()))
                    .count();

            long totalCheckins = reservaRepository.countByEventoAndUtilizado(evento, true);
    
            int lotacaoMaxima = evento.getCapacidadeDisponivel() + (int) reservasAtivas;
    
            long noShow = reservasAtivas - totalCheckins; 
    
            double taxaComparecimento = (reservasAtivas > 0) ? ((double) totalCheckins / reservasAtivas) * 100 : 0.0;

            dadosRelatorio.add(new RelatorioDTO(
                evento.getNome(), 
                evento.getCategoria(), 
                lotacaoMaxima, 
                reservasAtivas, 
                totalCheckins, 
                noShow, 
                taxaComparecimento
            ));
        }

        model.addAttribute("relatorios", dadosRelatorio);

        return "relatorio"; 
    }

    @GetMapping("/meus-ingressos")
    public String exibirMeusIngressos(Model model) {

        List<Reserva> reservasAtivas = reservaRepository.findAll().stream()
                .filter(r -> !"Cancelado".equals(r.getStatus()))
                .toList();
        
        model.addAttribute("reservas", reservasAtivas);
        
        return "meus-ingressos";
    }

    @PostMapping("/reservas/cancelar/{id}")
    public String cancelarReserva(@PathVariable Long id) {
        
        var reservaOpt = reservaRepository.findById(id);

        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            Evento evento = reserva.getEvento();
            
            boolean eventoJaPassou = evento.getData().isBefore(LocalDateTime.now());
            
            boolean ingressoJaUtilizado = reserva.isUtilizado();

            if (eventoJaPassou || ingressoJaUtilizado) {
                return "redirect:/meus-ingressos?erro=bloqueado";
            }
            
            if (reserva.getStatus().equals("Ativo")) {
                reserva.setStatus("Cancelado");
                evento.devolverIngresso();

                repository.save(evento);
                reservaRepository.save(reserva);
            }
        }

        return "redirect:/meus-ingressos";
    }
}