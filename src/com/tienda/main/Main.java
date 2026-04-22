package com.tienda.main;

import com.tienda.view.MenuPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}