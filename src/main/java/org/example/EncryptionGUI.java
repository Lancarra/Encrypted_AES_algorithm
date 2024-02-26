package org.example;

import org.example.TestTaker;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncryptionGUI extends JFrame {
    private JTextField textField;
    private JTextField keyField;
    private JComboBox<String> methodComboBox;
    private JTextArea resultArea;
    private JButton browseButton; // Добавляем кнопку для выбора файла

    private String filePath; // Переменная для хранения пути к файлу

    public EncryptionGUI() {
        setTitle("Encryption/Decryption System");
        setSize(400, 350); // Увеличил высоту для вмещения кнопки
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel textLabel = new JLabel("Enter text:");
        textLabel.setBounds(20, 20, 100, 20);
        add(textLabel);

        textField = new JTextField();
        textField.setBounds(120, 20, 250, 20);
        add(textField);

        JLabel keyLabel = new JLabel("Enter key:");
        keyLabel.setBounds(20, 50, 100, 20);
        add(keyLabel);

        keyField = new JTextField();
        keyField.setBounds(120, 50, 250, 20);
        add(keyField);

        JLabel methodLabel = new JLabel("Select method:");
        methodLabel.setBounds(20, 80, 100, 20);
        add(methodLabel);

        String[] methods = {"ECB", "CBC", "CFB"};
        methodComboBox = new JComboBox<>(methods);
        methodComboBox.setBounds(120, 80, 100, 20);
        add(methodComboBox);

        JButton encryptButton = new JButton("Encrypt/Decrypt");
        encryptButton.setBounds(20, 110, 150, 30);
        add(encryptButton);

        browseButton = new JButton("Browse");
        browseButton.setBounds(280, 110, 90, 30);
        add(browseButton);

        resultArea = new JTextArea();
        resultArea.setBounds(20, 150, 350, 150);
        resultArea.setEditable(false);
        add(resultArea);

        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                String key = keyField.getText();
                String method = (String) methodComboBox.getSelectedItem();
                try {
                    byte[] encryptedBytes = TestTaker.encrypt(text, key, method);
                    writeToFile(encryptedBytes);

                    String decrypted = TestTaker.decrypt(encryptedBytes, key, method);
                    resultArea.setText(decrypted);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                }
            }
        });
    }

    private void writeToFile(byte[] encryptedBytes) {
        if (filePath != null && !filePath.isEmpty()) {
            try (FileOutputStream writer = new FileOutputStream(filePath)) {
                writer.write(encryptedBytes);
                resultArea.setText("Encrypted data written to file: " + filePath);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "File record error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            resultArea.setText("Please select a file path.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EncryptionGUI gui = new EncryptionGUI();
                gui.setVisible(true);
            }
        });
    }
}