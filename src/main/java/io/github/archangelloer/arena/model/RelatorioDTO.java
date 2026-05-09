package io.github.archangelloer.arena.model; 

public class RelatorioDTO {
    
    private String nomeEvento;
    private long totalReservas;
    private long totalCheckins;
    private long noShow;
    private double taxaComparecimento;

    public RelatorioDTO(String nome, long reservas, long checkins, long noShow, double taxa) {
        this.nomeEvento = nome;
        this.totalReservas = reservas;
        this.totalCheckins = checkins;
        this.noShow = noShow;
        this.taxaComparecimento = taxa;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public long getTotalReservas() {
        return totalReservas;
    }

    public void setTotalReservas(long totalReservas) {
        this.totalReservas = totalReservas;
    }

    public long getTotalCheckins() {
        return totalCheckins;
    }

    public void setTotalCheckins(long totalCheckins) {
        this.totalCheckins = totalCheckins;
    }

    public long getNoShow() {
        return noShow;
    }

    public void setNoShow(long noShow) {
        this.noShow = noShow;
    }

    public double getTaxaComparecimento() {
        return taxaComparecimento;
    }

    public void setTaxaComparecimento(double taxaComparecimento) {
        this.taxaComparecimento = taxaComparecimento;
    }
}