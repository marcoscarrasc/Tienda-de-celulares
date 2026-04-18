package com.tienda.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.JFrame;

public class MenuPrincipal extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnRegistro, btnVenta;

    public MenuPrincipal() {
        setTitle("Sistema de Ventas de Celulares");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Superior: Botones
        JPanel panelBotones = new JPanel();
        btnRegistro = new JButton("Registrar Celular");
        btnVenta = new JButton("Realizar Venta");
        panelBotones.add(btnRegistro);
        panelBotones.add(btnVenta);
        add(panelBotones, BorderLayout.NORTH);

        // Centro: Tabla
        String[] columnas = {"Marca", "Modelo", "Precio (S/)", "Stock"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // EVENTO: REGISTRO
        btnRegistro.addActionListener(e -> registrar());

        // EVENTO: VENTA
        btnVenta.addActionListener(e -> vender());
    }

    private void registrar() {
        JTextField mrc = new JTextField();
        JTextField mod = new JTextField();
        JTextField pre = new JTextField();
        JTextField stk = new JTextField();

        Object[] msg = {"Marca:", mrc, "Modelo:", mod, "Precio:", pre, "Stock:", stk};
        int op = JOptionPane.showConfirmDialog(this, msg, "Nuevo Celular", JOptionPane.OK_CANCEL_OPTION);

        if (op == JOptionPane.OK_OPTION) {
            modelo.addRow(new Object[]{mrc.getText(), mod.getText(), pre.getText(), stk.getText()});
        }
    }

    private void vender() {
        // 1. Verificar si hay datos en la tabla para vender
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay celulares registrados para vender.");
            return;
        }

        // 2. Crear una lista de opciones para el seleccionable (Marcas de la tabla)
        String[] opcionesMarcas = new String[modelo.getRowCount()];
        for (int i = 0; i < modelo.getRowCount(); i++) {
            // Guardamos Marca + Modelo para que el usuario sepa qué elige
            opcionesMarcas[i] = modelo.getValueAt(i, 0).toString() + " - " + modelo.getValueAt(i, 1).toString();
        }

        // 3. Componentes del Formulario
        JComboBox<String> cbCelulares = new JComboBox<>(opcionesMarcas);
        JTextField txtCliente = new JTextField();
        JTextField txtCantidad = new JTextField("1");
        JLabel lblDetalles = new JLabel("Seleccione un producto para ver el precio.");

        // Acción para actualizar el precio en tiempo real al cambiar la selección
        cbCelulares.addActionListener(e -> {
            int index = cbCelulares.getSelectedIndex();
            if (index != -1) {
                String precio = modelo.getValueAt(index, 2).toString();
                String stock = modelo.getValueAt(index, 3).toString();
                lblDetalles.setText("Precio: S/ " + precio + " | Stock: " + stock);
            }
        });
        
        // Disparar el evento una vez para que muestre el precio del primer elemento por defecto
        cbCelulares.setSelectedIndex(0);

        Object[] formularioVenta = {
            "Seleccione Celular:", cbCelulares,
            "", lblDetalles,
            "\nNombre del Cliente:", txtCliente,
            "Cantidad a comprar:", txtCantidad
        };

        // 4. Mostrar el Diálogo
        int opcion = JOptionPane.showConfirmDialog(this, formularioVenta, "Realizar Venta", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            try {
                int filaSeleccionada = cbCelulares.getSelectedIndex();
                int cantidadAVender = Integer.parseInt(txtCantidad.getText());
                String nombreCliente = txtCliente.getText();

                // Obtener datos de la fila que corresponde a la selección del ComboBox
                String marca = modelo.getValueAt(filaSeleccionada, 0).toString();
                String mod = modelo.getValueAt(filaSeleccionada, 1).toString();
                double precioUnit = Double.parseDouble(modelo.getValueAt(filaSeleccionada, 2).toString());
                int stockActual = Integer.parseInt(modelo.getValueAt(filaSeleccionada, 3).toString());

                // 5. Validaciones
                if (nombreCliente.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese el nombre del cliente.");
                    return;
                }
                if (cantidadAVender > stockActual) {
                    JOptionPane.showMessageDialog(this, "No hay suficiente stock.");
                    return;
                }

                // 6. Procesar Venta y Actualizar Tabla
                double importeTotal = precioUnit * cantidadAVender;
                modelo.setValueAt(stockActual - cantidadAVender, filaSeleccionada, 3);

                // 7. Generar Boleta
                mostrarBoleta(nombreCliente, marca + " " + mod, cantidadAVender, precioUnit, importeTotal);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser un número.");
            }
        }
    }

    // Método auxiliar para imprimir la boleta
    private void mostrarBoleta(String cliente, String prod, int cant, double pUnit, double total) {
        String ticket = "==============================\n" +
                        "       BOLETA DE VENTA        \n" +
                        "==============================\n" +
                        "Cliente: " + cliente.toUpperCase() + "\n" +
                        "Producto: " + prod + "\n" +
                        "Cant: " + cant + "  x  S/ " + pUnit + "\n" +
                        "------------------------------\n" +
                        "TOTAL A PAGAR: S/ " + total + "\n" +
                        "==============================\n";
        
        JTextArea area = new JTextArea(ticket);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
    }
}