package ui;

import api.CurrencyAPI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ConverterUI extends JFrame {

    private JTextField amountField;
    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JLabel resultLabel;

    private BufferedImage bgImage;

    private String[] currencies = {
            "INR", "USD", "EUR", "GBP", "AUD", "JPY", "CAD", "SGD"
    };

    public ConverterUI() {
        setTitle("Live Currency Converter");
        setSize(420, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load background image safely
        try {
            bgImage = ImageIO.read(
                    getClass().getClassLoader().getResource("money_bg.jpg")
            );
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Image not found in src/resource/");
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        JLabel title = new JLabel("💱 Currency Converter", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        inputPanel.setOpaque(false);

        amountField = new JTextField();
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        fromCurrency = new JComboBox<>(currencies);
        toCurrency = new JComboBox<>(currencies);

        JButton convertButton = new JButton("Convert");
        convertButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        convertButton.setFocusPainted(false);
        convertButton.setBackground(new Color(60, 120, 200));
        convertButton.setForeground(Color.WHITE);

        resultLabel = new JLabel("Result: ", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setForeground(Color.WHITE);

        // Result card panel with background image
        JPanel resultCard = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        resultCard.setPreferredSize(new Dimension(300, 70));
        resultCard.setLayout(new BorderLayout());
        resultCard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        resultCard.add(resultLabel, BorderLayout.CENTER);

        inputPanel.add(new JLabel("Enter Amount:", SwingConstants.CENTER));
        inputPanel.add(amountField);
        inputPanel.add(fromCurrency);
        inputPanel.add(toCurrency);
        inputPanel.add(convertButton);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(resultCard, BorderLayout.SOUTH);

        convertButton.addActionListener(e -> convert());

        add(mainPanel);
    }

    private void convert() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = fromCurrency.getSelectedItem().toString();
            String to = toCurrency.getSelectedItem().toString();

            double rate = CurrencyAPI.getRate(from, to);

            if (rate == -1) {
                resultLabel.setText("Error fetching rate");
                return;
            }

            double result = Math.round(amount * rate * 100.0) / 100.0;
            resultLabel.setText("Result: " + result + " " + to);

        } catch (Exception e) {
            resultLabel.setText("Enter valid amount");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ConverterUI().setVisible(true);
        });
    }
}
