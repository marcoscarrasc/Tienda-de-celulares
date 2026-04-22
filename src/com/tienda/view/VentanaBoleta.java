package com.tienda.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaBoleta extends JDialog {

    public VentanaBoleta(JFrame padre, String modeloNombre, double totalPagar, String cliente, 
                         String dni, DefaultTableModel modeloTab, int fila, String obsequio, int cant) {
        super(padre, "Comprobante de Pago", true);
        setSize(420, 600);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout(10, 10));
        
        // --- CÁLCULOS TRIBUTARIOS ---
        double subtotal = totalPagar / 1.18;
        double igv = totalPagar - subtotal;

        // --- DISEÑO DEL ÁREA DE TEXTO ---
        JTextArea areaBoleta = new JTextArea();
        areaBoleta.setEditable(false);
        areaBoleta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaBoleta.setBackground(new Color(255, 255, 225)); // Color crema (papel)
        areaBoleta.setMargin(new Insets(20, 20, 20, 20));

        // Construcción del contenido del ticket
        StringBuilder sb = new StringBuilder();
        sb.append("==========================================\n");
        sb.append("           PHONE STORE - BOLETA           \n");
        sb.append("==========================================\n\n");
        sb.append(" CLIENTE: ").append(cliente.toUpperCase()).append("\n");
        sb.append(" DNI:     ").append(dni).append("\n");
        sb.append(" FECHA:   ").append(new java.util.Date().toString().substring(0, 19)).append("\n");
        sb.append("------------------------------------------\n");
        sb.append(String.format(" %-20s %5s %12s\n", "PRODUCTO", "CANT", "IMPORTE"));
        sb.append("------------------------------------------\n");
        
        // Detalle del producto
        sb.append(String.format(" %-20s %5d %12.2f\n", modeloNombre, cant, totalPagar));
        
        sb.append("------------------------------------------\n");
        sb.append(String.format(" SUB-TOTAL:           S/ %15.2f\n", subtotal));
        sb.append(String.format(" IGV (18%%):           S/ %15.2f\n", igv));
        sb.append(String.format(" TOTAL A PAGAR:       S/ %15.2f\n", totalPagar));
        sb.append("------------------------------------------\n");

        // Sección de Obsequios (Colores Cálidos)
        if (obsequio != null && !obsequio.isEmpty()) {
            sb.append("\n ¡FELICIDADES! PROMOCIÓN APLICADA:\n");
            sb.append(" REGALO: ").append(obsequio.toUpperCase()).append("\n");
            sb.append("------------------------------------------\n");
        }

        sb.append("\n    ¡GRACIAS POR SU PREFERENCIA!       \n");
        sb.append("==========================================");

        areaBoleta.setText(sb.toString());
        add(new JScrollPane(areaBoleta), BorderLayout.CENTER);

        // --- PANEL DE BOTONES (ESTILO CÁLIDO) ---
        JPanel pnlSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlSur.setBackground(MenuPrincipal.AMBAR_FONDO);

        JButton btnFinalizar = new JButton("Finalizar e Imprimir");
        btnFinalizar.setBackground(MenuPrincipal.NARANJA_PRIMARIO);
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFinalizar.setPreferredSize(new Dimension(200, 40));
        btnFinalizar.setFocusPainted(false);

        btnFinalizar.addActionListener(e -> {
            // Actualización de Stock en el modelo de la tabla principal
            try {
                int stockActual = Integer.parseInt(modeloTab.getValueAt(fila, 4).toString());
                modeloTab.setValueAt(stockActual - cant, fila, 4);
                JOptionPane.showMessageDialog(this, "Venta Exitosa. Stock actualizado.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar stock.");
            }
        });

        pnlSur.add(btnFinalizar);
        add(pnlSur, BorderLayout.SOUTH);
    }
}