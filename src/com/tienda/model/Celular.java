package com.tienda.model;

public class Celular {
    private String marca;
    private String modelo;
    private String caracteristicas;
    private double precio;
    private int stock;

    // Constructor corregido
    public Celular(String marca, String modelo, String caracteristicas, double precio, int stock) {
        this.marca = marca;
        this.modelo = modelo;
        this.caracteristicas = caracteristicas;
        this.precio = precio;
        this.stock = stock;
    }

    // Métodos para obtener los datos (Getters)
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getCaracteristicas() { return caracteristicas; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    
    // Método para actualizar el stock tras una venta
    public void setStock(int stock) { this.stock = stock; }
}