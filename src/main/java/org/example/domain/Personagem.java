package org.example.domain;

import org.example.criatura.Cobra;
import org.example.interfaces.PersonagemInterface;
import org.example.itens.Inventario;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


public abstract class Personagem {

    private String nome;

    private Double vida;

    private Double fome;

    private Double sede;

    private Double energia;

    private Double sanidade;

    private Inventario inventario;

    private double[] localizacao;

    private boolean estaEnvenenado;

    protected Personagem(String nome) {
        this.nome = nome;
        this.energia = 100.0;
        this.fome = 50.0;
        this.vida = 100.0;
        this.sede = 50.0;
        this.sanidade = 100.0;
        this.inventario = new Inventario(6, Collections.emptyList(), 0.0);
        this.localizacao = new double[]{0.0, 0.0};
        this.estaEnvenenado = false;

    }

    public Personagem() { }

    public Double getEnergia() {
        return energia;
    }

    public void aumentarEnergia(Double energia) {

        if (energia <=0) {
            throw new IllegalArgumentException("A energia deve ser aumentada, e por isso só aceita valores maiores que 0!");
        }

        this.energia += energia;
    }

    public void diminuirEnergia(Double energia) {
        if (energia <=0) {
            throw new IllegalArgumentException("Você precisa passar um valor de energia maior que 0!");
        }

        this.energia -= energia;
    }

    public Double getFome() {
        return fome;
    }

    public void aumentarFome(Double fome) {

        if (fome <=0) {
            throw new IllegalArgumentException("A fome deve ser aumentada, e por isso só aceita valores maiores que 0!");
        }

        this.fome += fome;
    }

    public void diminuirFome(Double fome) {

        if (fome <=0) {
            throw new IllegalArgumentException("Você precisa passar um valor de fome maior que 0!");
        }

        this.fome -= fome;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public double[] getLocalizacao() {
        return localizacao;
    }

    public void alterarLocalizacao(double[] localizacao) {
        this.localizacao = localizacao;
    }

    public String getNome() {
        return nome;
    }

    public void alterarNomePersonagem(String nome) {
        this.nome = nome;
    }

    public Double getSede() {
        return sede;
    }

    public void aumentarSede(Double sede) {

        if (sede <=0) {
            throw new IllegalArgumentException("A sede deve ser aumentada, e por isso só aceita valores maiores que 0!");
        }

        this.sede += sede;
    }

    public void diminuirSede(Double sede) {

        if (sede <=0) {
            throw new IllegalArgumentException("Você precisa passar um valor de sede maior que 0!");
        }

        this.sede -= sede;
    }

    public Double getSanidade() {
        return sanidade;
    }

    public void aumentarSanidade(Double sanidade) {

        if (sanidade <=0) {
            throw new IllegalArgumentException("A sanidade deve ser aumentada, e por isso só aceita valores maiores que 0!");
        }

        this.sanidade += sanidade;
    }

    public void diminuirSanidade(Double sanidade) {

        if (sanidade <=0) {
            throw new IllegalArgumentException("Você precisa passar um valor de sanidade maior que 0!");
        }

        this.sanidade -= sanidade;
    }

    public Double getVida() {
        return this.vida;
    }

    public void aumentarVida(Double vida) {

        if (vida <=0) {
            throw new IllegalArgumentException("A sanidade deve ser aumentada, e por isso só aceita valores maiores que 0!");
        }

        this.vida += vida;
    }

    public void diminuirVida(Double vida) {

        if (sanidade <=0) {
            throw new IllegalArgumentException("Você precisa passar um valor de vida maior que 0!");
        }

        this.vida -= vida;
    }

    public boolean getEstaEnvenedo(){
        return this.estaEnvenenado;
    }

    public void envenenar() {
        this.estaEnvenenado = true;
    }

    public void CuradoDoVeneno(){
         this.estaEnvenenado = false;
    }

    public void danoPorEnvenenamento(List<Criatura> listaDeCriaturas, Double getDificuldadeAmbiente){

        //recebe uma lista de criaturas que existem no jogo, filtra essa lista para apenas cobras;
        Optional<Cobra> cobra = listaDeCriaturas.stream()
                .filter(a -> a instanceof Cobra)
                .map(c -> ((Cobra) c))
                .findAny();
        //verifica se tem cobra, acho q n precisa disso
        cobra.ifPresent(c-> {

            diminuirVida(c.getDanoDeAtaque()*getDificuldadeAmbiente);
            System.out.printf("Você sofreu %f de dano", c.getDanoDeAtaque() * 0.5);

        });
    }



}
