package edu.unisabana.dyas.samples.entities;

import java.io.Serializable;

public class ItemRentado implements Serializable {

    private int id;
    private Item item;

    // Usamos String para evitar problemas de parsing con SQLite
    private String fechainiciorenta;
    private String fechafinrenta;

    public ItemRentado(int id, Item item, String fechainiciorenta, String fechafinrenta) {
        this.id = id;
        this.item = item;
        this.fechainiciorenta = fechainiciorenta;
        this.fechafinrenta = fechafinrenta;
    }

    public ItemRentado() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getFechainiciorenta() {
        return fechainiciorenta;
    }

    public void setFechainiciorenta(String fechainiciorenta) {
        this.fechainiciorenta = fechainiciorenta;
    }

    public String getFechafinrenta() {
        return fechafinrenta;
    }

    public void setFechafinrenta(String fechafinrenta) {
        this.fechafinrenta = fechafinrenta;
    }

    @Override
    public String toString() {
        return "ItemRentado{" +
                "id=" + id +
                ", item=" + item +
                ", fechainiciorenta='" + fechainiciorenta + '\'' +
                ", fechafinrenta='" + fechafinrenta + '\'' +
                '}';
    }
}