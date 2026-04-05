package io.github.archangelloer.arena.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.github.archangelloer.arena.model.Evento;
import io.github.archangelloer.arena.repository.EventoRepository;

@Controller
public class EventoController {

    @Autowired
    private EventoRepository repository;

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
    
        // Validação manual: verifica se a data do evento é antes de "agora"
        if (eventoRecebido.getData() != null && eventoRecebido.getData().isBefore(LocalDateTime.now())) {
            // 1. Cria a variável 'erroData' com a mensagem
            model.addAttribute("erroData", "A data do evento não pode ser no passado!");
            // 2. Devolve o objeto 'novoEvento' para o formulário não limpar os campos
            model.addAttribute("novoEvento", eventoRecebido); 
            // 3. Recarrega a página de cadastro em vez de salvar
            return "cadastro";
        }
        repository.save(eventoRecebido);
        return "redirect:/";
    }

}