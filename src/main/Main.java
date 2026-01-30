package main;

import ui.ConverterUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            ConverterUI app = new ConverterUI();
            app.setVisible(true);
        });
    }
}

