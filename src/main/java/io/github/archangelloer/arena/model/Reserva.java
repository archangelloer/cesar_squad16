package io.github.archangelloer.arena.model;

import jakarta.persistence.*;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    private String nome;

    private String codigoTexto;

    private boolean utilizado;

    public Reserva() {}

    public Reserva(Evento evento, String nome, String codigoTexto) {
        this.evento = evento;
        this.nome = nome;
        this.codigoTexto = codigoTexto;
        this.utilizado = false; 
    }

    public Long getId() {
        return id; 
    }
    public void setId(Long id) {
        this.id = id; 
    }

    public Evento getEvento() { 
        return evento; 
    }
    public void setEvento(Evento evento) { 
        this.evento = evento; 
    }

    public String getNome() { 
        return nome; 
    }
    public void setNome(String nome) { 
        this.nome = nome; 
    }

    public String getCodigoTexto() { 
        return codigoTexto; 
    }
    public void setCodigoTexto(String codigoTexto) {
        this.codigoTexto = codigoTexto; 
    }

    public boolean isUtilizado() { 
        return utilizado; 
    }
    public void setUtilizado(boolean utilizado) { 
        this.utilizado = utilizado; 
    }
}