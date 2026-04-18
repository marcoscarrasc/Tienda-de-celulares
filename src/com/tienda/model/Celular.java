package com.tienda.model;

public class Celular {
    private String marca;
    private String modelo;
    private double precio;
    private int stock;

    public Celular(String marca, String modelo, double precio, int stock) {
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters y Setters
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}