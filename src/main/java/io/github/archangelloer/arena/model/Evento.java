package io.github.archangelloer.arena.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalDateTime data;
    private String categoria;
    private int capacidadeDisponivel;

    public Evento(){}
    
    public Evento(String nome, LocalDateTime data, String categoria, int capacidadeDisponivel){
        this.nome = nome;
        this.data = data;
        this.categoria = categoria;
        this.capacidadeDisponivel = capacidadeDisponivel;
    }

    public Long getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public LocalDateTime getData(){
        return this.data;
    }

    public String getCategoria(){
        return this.categoria;
    }

    public int getCapacidadeDisponivel(){
        return this.capacidadeDisponivel;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setData(LocalDateTime data){
        this.data = data;
    }

    public void setCategoria(String categoria){
        this.categoria = categoria;
    }

    public void setCapacidadeDisponivel(int capacidadeDisponivel){
        this.capacidadeDisponivel = capacidadeDisponivel;
    }

    public void reservarIngresso(){
        if(capacidadeDisponivel>0){
            capacidadeDisponivel = capacidadeDisponivel-1;
            System.out.println("Reserva Confirmada!");
        } else if(capacidadeDisponivel<=0){
            System.out.println("Ingressos Esgotados!");
        }
    }
}