package com.tienda.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormularioVenta extends JDialog {
    private JComboBox<String> cbMod;
    private JLabel lblPrecioUnit, lblTotalVenta;
    private JSpinner spinCant;
    private JTextField txtCliente, txtDni;
    private DefaultTableModel modeloInventario;

    public FormularioVenta(MenuPrincipal padre, DefaultTableModel modelo) {
        super(padre, "Nueva Venta", true);
        this.modeloInventario = modelo;
        configurarVentana(padre);
        inicializarComponentes();
        vincularEventos();
        actualizarMonto(); // Cálculo inicial
    }

    private void configurarVentana(JFrame padre) {
        setSize(450, 550);
        setLocationRelativeTo(padre);
        getContentPane().setBackground(MenuPrincipal.AMBAR_FONDO);
        
        // 1. Añadimos un margen alrededor de todo el formulario para que no toque los bordes
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // Mantenemos tu GridLayout
        setLayout(new GridLayout(8, 2, 15, 15)); 
    }

    private void inicializarComponentes() {
        cbMod = new JComboBox<>();
        for (int i = 0; i < modeloInventario.getRowCount(); i++) {
            cbMod.addItem(modeloInventario.getValueAt(i, 1).toString());
        }

        lblPrecioUnit = new JLabel("0.00");
        lblPrecioUnit.setFont(new Font("Arial", Font.PLAIN, 14));

        spinCant = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        
        // 2. Definimos columnas para evitar que los campos se deformen
        txtCliente = new JTextField(20);
        txtDni = new JTextField(10);
        
        lblTotalVenta = new JLabel("0.00");
        lblTotalVenta.setFont(new Font("Arial", Font.BOLD, 24));
        lblTotalVenta.setForeground(MenuPrincipal.NARANJA_PRIMARIO);

        // Etiquetas con espacio a la izquierda
        add(new JLabel("Modelo:")); add(cbMod);
        add(new JLabel("Precio Unit:")); add(lblPrecioUnit);
        add(new JLabel("Cantidad:")); add(spinCant);
        add(new JLabel("Cliente:")); add(txtCliente);
        add(new JLabel("DNI:")); add(txtDni);
        add(new JLabel("TOTAL:")); add(lblTotalVenta);

        // 3. Botones con mejor estilo
        JButton btnC = new JButton("Cancelar");
        btnC.setFont(new Font("Arial", Font.BOLD, 13));
        btnC.addActionListener(e -> dispose());
        add(btnC);

        JButton btnV = new JButton("Confirmar");
        btnV.setBackground(MenuPrincipal.NARANJA_PRIMARIO);
        btnV.setForeground(Color.WHITE);
        btnV.setFont(new Font("Arial", Font.BOLD, 13));
        btnV.addActionListener(e -> procesarVenta());
        add(btnV);
    }

    private void vincularEventos() {
        cbMod.addActionListener(e -> actualizarMonto());
        spinCant.addChangeListener(e -> actualizarMonto());
    }

    private void actualizarMonto() {
        int idx = cbMod.getSelectedIndex();
        if (idx != -1) {
            try {
                double precio = Double.parseDouble(modeloInventario.getValueAt(idx, 3).toString());
                int cant = (int) spinCant.getValue();
                lblPrecioUnit.setText(String.format("%.2f", precio));
                lblTotalVenta.setText(String.format("%.2f", precio * cant));
            } catch (Exception ex) {
                lblPrecioUnit.setText("0.00");
                lblTotalVenta.setText("0.00");
            }
        }
    }

    private void procesarVenta() {
        if (txtCliente.getText().trim().isEmpty() || txtDni.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los datos del cliente", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Reemplazo de coma por punto para evitar errores de parseo según el sistema
        double total = Double.parseDouble(lblTotalVenta.getText().replace(",", "."));
        String regalo = calcularRegalo(total);
        
        if (!"Sin obsequio".equals(regalo)) {
            JOptionPane.showMessageDialog(this, "¡Felicidades! Se ha ganado un obsequio: " + regalo);
        }

        dispose();
        
        // Abrir la boleta
        new VentanaBoleta((JFrame)getParent(), 
                        cbMod.getSelectedItem().toString(), 
                        total, 
                        txtCliente.getText(), 
                        txtDni.getText(), 
                        modeloInventario, 
                        cbMod.getSelectedIndex(), 
                        regalo, 
                        (int)spinCant.getValue()).setVisible(true);
    }

    private String calcularRegalo(double total) {
        String regalo = "Sin obsequio";
        double maxMonto = 0;
        
        // Verificación de seguridad por si el modelo de regalos no está inicializado
        if (FormularioRegalo.modeloRegalos != null) {
            for (int i = 0; i < FormularioRegalo.modeloRegalos.getRowCount(); i++) {
                try {
                    double min = Double.parseDouble(FormularioRegalo.modeloRegalos.getValueAt(i, 0).toString());
                    if (total >= min && min >= maxMonto) {
                        maxMonto = min;
                        regalo = FormularioRegalo.modeloRegalos.getValueAt(i, 1).toString();
                    }
                } catch (Exception e) {
                    continue; 
                }
            }
        }
        return regalo;
    }
}