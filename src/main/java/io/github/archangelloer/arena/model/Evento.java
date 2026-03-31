package io.github.archangelloer.arena.model;

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
    private String data;
    private String categoria;
    private int capacidade;

    public Evento(){}
    
    public Evento(String nome, String data, String categoria, int capacidade){
        this.nome = nome;
        this.data = data;
        this.categoria = categoria;
        this.capacidade = capacidade;
    }

    public Long getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getData(){
        return this.data;
    }

    public String getCategoria(){
        return this.categoria;
    }

    public int getCapacidade(){
        return this.capacidade;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setData(String data){
        this.data = data;
    }

    public void setCategoria(String categoria){
        this.categoria = categoria;
    }

    public void setCapacidade(int capacidade){
        this.capacidade = capacidade;
    }

    public void reservarIngresso(){
        if(capacidade>0){
            capacidade = capacidade-1;
            System.out.println("Reserva Confirmada!");
        } else if(capacidade<=0){
            System.out.println("Ingressos Esgotados!");
        }
    }
}