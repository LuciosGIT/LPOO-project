package org.example.itens;

import org.example.domain.Item;

import javax.naming.NameNotFoundException;
import java.util.List;

public class Inventario {

    private List<Item> listaDeItems;

    private Double pesoTotal;

    private Integer capacidadeMaxima;

    public Inventario(Integer capacidadeMaxima, List<Item> listaDeItems, Double pesoTotal) {
        this.capacidadeMaxima = capacidadeMaxima;
        this.listaDeItems = listaDeItems;
        this.pesoTotal = pesoTotal;
    }

    public Inventario() {}

    public boolean temEspaco() {
        return this.listaDeItems.size() < this.capacidadeMaxima;
    }

    public void adicionarItem(Item item) {
        if (this.temEspaco()) {
            this.listaDeItems.add(item);
            this.pesoTotal += item.getPeso();
        }
        else{
            System.out.printf("Seu inventário está cheio");
        }
    }

    public void removerItem(String nomeItem) throws NameNotFoundException {
        boolean itemEncontrado = false;
        for (Item item : this.listaDeItems) {
            if (item.getNomeItem().equals(nomeItem)) {
                listaDeItems.remove(item);
                this.pesoTotal -= item.getPeso();
                itemEncontrado = true;
                break;
            }
        }
        if (!itemEncontrado) {
            throw new NameNotFoundException("Item não encontrado: " + nomeItem);
        }
    }

    public void usarItem(String nomeItem) throws NameNotFoundException {
        boolean itemEncontrado = false;

        for (Item item : this.listaDeItems) {
            if (item.getNomeItem().equals(nomeItem)) {
                itemEncontrado = true;

                if (item instanceof Agua || item instanceof Alimentos || item instanceof Ferramentas) {
                    item.usar();
                    this.removerItem(nomeItem);
                } else {
                    System.out.println("Esse item não pode ser usado diretamente.");
                }
                break;
            }
        }

        if (!itemEncontrado) {
            throw new NameNotFoundException("Item não encontrado: " + nomeItem);
        }
    }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public List<Item> getListaDeItems() {
        return listaDeItems;
    }

    public void setListaDeItems(List<Item> listaDeItems) {
        this.listaDeItems = listaDeItems;
    }

    public Double getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(Double pesoTotal) {
        this.pesoTotal = pesoTotal;
    }
}
