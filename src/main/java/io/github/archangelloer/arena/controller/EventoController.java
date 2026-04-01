package io.github.archangelloer.arena.controller;

import io.github.archangelloer.arena.model.Evento;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
public class EventoController {

    @GetMapping("/")
    public String listarEventos(Model model){
        Evento evento1 = new Evento("Santa Cruz x Retrô", LocalDateTime.now(), "Esporte", 20000);
        Evento evento2 = new Evento("Evento de Teste", LocalDateTime.now(), "Shows", 0);
        Evento evento3 = new Evento("Meu Malvado Favorito", LocalDateTime.now(), "Filme", 100);

        List<Evento> minhaLista = Arrays.asList(evento1, evento2, evento3);

        model.addAttribute("eventos", minhaLista);

        return "index";
    }

}