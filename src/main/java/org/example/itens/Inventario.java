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

    public  boolean temEspaco() {
        if (this.listaDeItems.size() == this.capacidadeMaxima) {
            return true;
        }
        else return false;
    }
    public void adicionarItem(Item item) {
        if (this.temEspaco()) {
            this.listaDeItems.add(item);
            this.pesoTotal += item.getPeso();
        }
    }

    public void removerItem(String nomeItem) throws NameNotFoundException {
        boolean itemEncontrado = false;
        for (Item item : this.listaDeItems) {
            if (item.getTipoItem().getValue().equals(nomeItem)) {
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
            if (item.getTipoItem().getValue().equals(nomeItem)) {
                item.usar();
                itemEncontrado = true;
                break;  // Saia do loop após usar o item
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
