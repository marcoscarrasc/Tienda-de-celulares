package com.tienda.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MenuPrincipal extends JFrame {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBusqueda;
    private boolean usuarioLogueado = false;

    // Paleta de colores cálidos
    public static final Color NARANJA_PRIMARIO = new Color(255, 140, 0);
    public static final Color AMBAR_FONDO = new Color(255, 248, 225);
    public static final Color CREMA_DETALLE = new Color(255, 253, 231);

    public MenuPrincipal() {
        setTitle("Phone Store - Gestión de Ventas");
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(AMBAR_FONDO);
        setLayout(new BorderLayout(10, 10));

        // --- CABECERA (LOGO + BUSCADOR + ACCIONES) ---
        JPanel pnlCabecera = new JPanel(new BorderLayout());
        pnlCabecera.setBackground(NARANJA_PRIMARIO);
        pnlCabecera.setPreferredSize(new Dimension(100, 70));

        // 1. Logo y Buscador (Izquierda)
        JPanel pnlLogoBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        pnlLogoBusqueda.setOpaque(false);
        
        JLabel lblLogo = new JLabel("PHONE STORE");
        lblLogo.setFont(new Font("Arial Black", Font.ITALIC, 22));
        lblLogo.setForeground(Color.WHITE);
        
        txtBusqueda = new JTextField(15);
        txtBusqueda.setToolTipText("Buscar por modelo o marca...");
        
        pnlLogoBusqueda.add(lblLogo);
        pnlLogoBusqueda.add(new JLabel(" 🔍 "));
        pnlLogoBusqueda.add(txtBusqueda);

        // 2. Botones de Operación (Centro)
        JPanel pnlAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        pnlAcciones.setOpaque(false);
        
        JButton btnAdd = crearBotonEstilizado(" Registrar");
        JButton btnEdit = crearBotonEstilizado(" Editar");
        JButton btnGift = crearBotonEstilizado(" Regalos");
        JButton btnVend = crearBotonEstilizado(" Vender");
        
        pnlAcciones.add(btnAdd); 
        pnlAcciones.add(btnEdit); 
        pnlAcciones.add(btnGift); 
        pnlAcciones.add(btnVend);

        // 3. Botones de Sesión (Derecha - Iniciar y Salir JUNTOS)
        JPanel pnlDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlDerecha.setOpaque(false);
        
        JButton btnLogin = crearBotonEstilizado(" Iniciar Sesión");
        JButton btnSalir = crearBotonEstilizado(" Salir");
        
        pnlDerecha.add(btnLogin); // Agregado aquí para que estén juntos
        pnlDerecha.add(btnSalir);

        // Montar la cabecera
        pnlCabecera.add(pnlLogoBusqueda, BorderLayout.WEST);
        pnlCabecera.add(pnlAcciones, BorderLayout.CENTER);
        pnlCabecera.add(pnlDerecha, BorderLayout.EAST);
        add(pnlCabecera, BorderLayout.NORTH);

        // --- TABLA DE INVENTARIO ---
        modelo = new DefaultTableModel(new String[]{"Marca", "Modelo", "Caracteristica", "Precio", "Stock"}, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabla.setSelectionBackground(NARANJA_PRIMARIO);
        tabla.setSelectionForeground(Color.WHITE);
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(AMBAR_FONDO);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

        // --- LÓGICA DE FILTRADO ---
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        txtBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String filtro = txtBusqueda.getText();
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filtro));
            }
        });

        // --- EVENTOS ---
        btnLogin.addActionListener(e -> new Login(this).setVisible(true));
        
        btnGift.addActionListener(e -> {
            if(validarAcceso()) new FormularioRegalo(this).setVisible(true);
        });
        
        btnVend.addActionListener(e -> {
            if(validarAcceso()) {
                if(modelo.getRowCount() > 0) new FormularioVenta(this, modelo).setVisible(true);
                else JOptionPane.showMessageDialog(this, "No hay stock para vender.");
            }
        });

        btnAdd.addActionListener(e -> { if(validarAcceso()) gestionarCelular(-1); });

        btnEdit.addActionListener(e -> {
            if(validarAcceso()) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) gestionarCelular(tabla.convertRowIndexToModel(fila));
                else JOptionPane.showMessageDialog(this, "Seleccione un producto.");
            }
        });

        btnSalir.addActionListener(e -> System.exit(0));
    }

    public void cargarDatosTrasLogin() {
        this.usuarioLogueado = true;
        if (modelo.getRowCount() == 0) {
            modelo.addRow(new Object[]{"Samsung", "Galaxy S23", "128GB, 8GB RAM", 3200.0, 15});
            modelo.addRow(new Object[]{"Apple", "iPhone 15", "256GB, A16 Bionic", 4500.0, 10});
            modelo.addRow(new Object[]{"Xiaomi", "Redmi Note 13", "256GB, 12GB RAM", 1500.0, 20});
        }
        JOptionPane.showMessageDialog(this, "Sesión Iniciada con Éxito");
    }

    private boolean validarAcceso() {
        if (!usuarioLogueado) {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión para realizar esta operación.");
            return false;
        }
        return true;
    }

    private void gestionarCelular(int fila) {
        JTextField m1 = new JTextField(); JTextField m2 = new JTextField();
        JTextField c = new JTextField(); JTextField p = new JTextField();
        JTextField s = new JTextField();
        
        if (fila != -1) {
            m1.setText(modelo.getValueAt(fila, 0).toString()); m2.setText(modelo.getValueAt(fila, 1).toString());
            c.setText(modelo.getValueAt(fila, 2).toString()); p.setText(modelo.getValueAt(fila, 3).toString());
            s.setText(modelo.getValueAt(fila, 4).toString());
        }

        Object[] msg = {"Marca:", m1, "Modelo:", m2, "Detalle:", c, "Precio:", p, "Stock:", s};
        int r = JOptionPane.showConfirmDialog(this, msg, "Datos del Equipo", JOptionPane.OK_CANCEL_OPTION);
        
        if (r == JOptionPane.OK_OPTION) {
            try {
                Object[] data = {m1.getText(), m2.getText(), c.getText(), Double.parseDouble(p.getText()), Integer.parseInt(s.getText())};
                if (fila == -1) modelo.addRow(data);
                else for (int i = 0; i < 5; i++) modelo.setValueAt(data[i], fila, i);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: Verifique Precio/Stock"); }
        }
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(CREMA_DETALLE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}