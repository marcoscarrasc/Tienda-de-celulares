package com.tienda.view;

import javax.swing.*;
import java.awt.*;

public class Login extends JDialog {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private MenuPrincipal ventanaPadre;

    public Login(MenuPrincipal padre) {
        super(padre, "Acceso al Sistema", true);
        this.ventanaPadre = padre;
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setSize(350, 220);
        setLocationRelativeTo(null);
        getContentPane().setBackground(MenuPrincipal.AMBAR_FONDO);
        setLayout(new GridLayout(3, 2, 15, 15));
    }

    private void inicializarComponentes() {
        // Estilo reutilizable
        Font fuenteLabel = new Font("Arial", Font.BOLD, 13);
        Color colorTexto = new Color(139, 69, 19);

        // Campos
        add(crearLabel(" Usuario:", fuenteLabel, colorTexto));
        txtUser = new JTextField();
        add(txtUser);

        add(crearLabel(" Password:", fuenteLabel, colorTexto));
        txtPass = new JPasswordField();
        add(txtPass);

        // Botones
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setBackground(MenuPrincipal.NARANJA_PRIMARIO);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.addActionListener(e -> validarAcceso());

        add(btnCancelar);
        add(btnEntrar);
    }

    private JLabel crearLabel(String texto, Font f, Color c) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(f);
        lbl.setForeground(c);
        return lbl;
    }

    private void validarAcceso() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        if ("Admin".equals(user) && "00000".equals(pass)) {
            if (ventanaPadre != null) ventanaPadre.cargarDatosTrasLogin();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}