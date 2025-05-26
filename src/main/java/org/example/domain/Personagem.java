package org.example.domain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.ambientes.AmbienteFloresta;
import org.example.criatura.Cobra;
import org.example.interfaces.PersonagemInterface;
import org.example.itens.Inventario;

import java.util.*;


public abstract class Personagem implements PersonagemInterface{

    private String nome;

    private Double vida;

    private Double fome;

    private Double sede;

    private Double energia;

    private Double sanidade;

    private Inventario inventario;

    private Ambiente localizacao;


    protected double modificadorFome = 1.0;

    protected double modificadorSede = 1.0;

    private Double danoDeAtaque;

    public Personagem(String nome, Double danoDeAtaque) {
        this.nome = nome;
        this.energia = 100.0;
        this.fome = 80.0;
        this.vida = 100.0;
        this.sede = 70.0;
        this.sanidade = 100.0;
        this.inventario = new Inventario(5, new ArrayList<>(), 0.0);
        this.localizacao = new AmbienteFloresta();
        this.danoDeAtaque = danoDeAtaque;

    }

    public Personagem() { }


    public void aplicarEfeitosDoTurno() {
        // Aumenta fome e sede com o tempo
        fome = Math.min(fome + 10, 100);
        sede = Math.min(sede + 12, 100);
        sanidade = Math.max(sanidade - 5, 0);

        // Reduz energia conforme fome/sede aumentam
        if (fome > 70) {
            energia = Math.max(energia - 10, 0);
        }
        if (sede > 70) {
            energia = Math.max(energia - 10, 0);
        }

        // Reduz vida se fome ou sede estiver muito alta
        if (fome >= 90 || sede >= 90) {
            vida = Math.max(vida - 5, 0);
        }

        // Reduz vida se sanidade estiver baixa
        if (sanidade <= 20) {
            vida = Math.max(vida - 3, 0);
        }

        // Exibir status
        exibirStatus();
    }

    public void exibirStatus() {
        System.out.println("----- Novo Turno -----");
        System.out.println("Vida: " + vida);
        System.out.println("Energia: " + energia);
        System.out.println("Fome: " + fome);
        System.out.println("Sede: " + sede);
        System.out.println("Sanidade: " + sanidade);
        System.out.println("----------------------");
    }

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

        this.fome -= fome * modificadorFome;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Ambiente getLocalizacao() {
        return localizacao;
    }

    public void alterarLocalizacao(Ambiente localizacao) {
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

        this.sede -= sede * modificadorSede;
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

    public synchronized void aumentarVida(Double vida) {

        if (vida <=0) {
            throw new IllegalArgumentException("A sanidade deve ser aumentada, e por isso só aceita valores maiores que 0!");
        }

        this.vida += vida;
    }

    public synchronized void diminuirVida(Double vida) {

        if (sanidade <=0) {
            throw new IllegalArgumentException("Você precisa passar um valor de vida maior que 0!");
        }

        this.vida -= vida;
    }

    public abstract HashMap<String, TextureRegion> getSprites();
}