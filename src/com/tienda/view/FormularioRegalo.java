package com.tienda.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FormularioRegalo extends JDialog {
    // Definimos el modelo con las promociones iniciales
    public static DefaultTableModel modeloRegalos = new DefaultTableModel(
        new Object[][] {
            {1000.0, "Case"},
            {2000.0, "Cargador"},
            {3000.0, "Protector + Case + Audífonos"}
        }, 
        new String[]{"Monto Mínimo", "Obsequio"}
    );

    public FormularioRegalo(JFrame padre) {
        super(padre, "Configurar Promociones", true);
        setSize(450, 350);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout());

        // Tabla para gestionar las promociones
        JTable tablaRegalos = new JTable(modeloRegalos);
        add(new JScrollPane(tablaRegalos), BorderLayout.CENTER);

        // Panel para agregar nuevas promociones manualmente
        JPanel pnlInput = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Nueva Promoción"));
        JTextField txtMonto = new JTextField();
        JTextField txtRegalo = new JTextField();

        pnlInput.add(new JLabel(" Monto Mínimo:")); pnlInput.add(txtMonto);
        pnlInput.add(new JLabel(" Regalo:")); pnlInput.add(txtRegalo);
        add(pnlInput, BorderLayout.NORTH);

        // Botones de control
        JPanel pnlBotones = new JPanel();
        JButton btnAdd = new JButton("Agregar");
        JButton btnDel = new JButton("Eliminar Seleccionado");
        JButton btnCerrar = new JButton("Cerrar");

        btnAdd.addActionListener(e -> {
            try {
                double monto = Double.parseDouble(txtMonto.getText());
                modeloRegalos.addRow(new Object[]{monto, txtRegalo.getText()});
                txtMonto.setText(""); txtRegalo.setText("");
            } catch (Exception ex) { 
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un monto válido."); 
            }
        });

        btnDel.addActionListener(e -> {
            int fila = tablaRegalos.getSelectedRow();
            if(fila != -1) modeloRegalos.removeRow(fila);
            else JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.");
        });

        btnCerrar.addActionListener(e -> dispose());

        pnlBotones.add(btnAdd); pnlBotones.add(btnDel); pnlBotones.add(btnCerrar);
        add(pnlBotones, BorderLayout.SOUTH);
    }
}