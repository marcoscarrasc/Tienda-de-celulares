package com.tienda.view;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private JTextField user = new JTextField();
    private JPasswordField pass = new JPasswordField();
    private JButton btn = new JButton("Ingresar");

    public Login() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2));

        add(new JLabel(" Usuario:")); add(user);
        add(new JLabel(" Clave:")); add(pass);
        add(new JLabel("")); add(btn);

        btn.addActionListener(e -> {
            if(user.getText().equals("admin") && new String(pass.getPassword()).equals("1234")) {
                new MenuPrincipal().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error");
            }
        });
    }
}