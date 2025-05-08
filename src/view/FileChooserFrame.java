package view;

import controller.FileUploadController;
import model.FileUploadModel;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

@SuppressWarnings("serial")
public class FileChooserFrame extends JFrame {

    private final FileUploadModel model;
    private final FileUploadController controller;

    public FileChooserFrame() {
        setTitle("Selector de Archivos");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.model = new FileUploadModel();
        this.controller = new FileUploadController(model, this);

        JLabel titleLabel = new JLabel("Selecciona y sube tus archivos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        add(titleLabel, BorderLayout.NORTH);

        JButton chooseButton = new JButton("Elegir Archivos");
        chooseButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chooseButton.setBackground(new Color(60, 120, 180));
        chooseButton.setForeground(Color.WHITE);
        chooseButton.setPreferredSize(new Dimension(150, 40));
        chooseButton.addActionListener(e -> openFileChooser());

        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(chooseButton);
        add(centerPanel, BorderLayout.CENTER);

        JButton uploadButton = new JButton("Subir Archivos");
        uploadButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        uploadButton.setBackground(new Color(0, 150, 90));
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setPreferredSize(new Dimension(150, 40));
        uploadButton.addActionListener(e -> controller.uploadFiles());

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(uploadButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Todos los archivos", "*.*"));
        fileChooser.setDialogTitle("Selecciona varios archivos");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selected = fileChooser.getSelectedFiles();
            model.setSelectedFiles(selected);

            StringBuilder sb = new StringBuilder("Archivos seleccionados:\n");
            for (File f : selected) {
                sb.append(f.getName()).append("\n");
            }

            JOptionPane.showMessageDialog(this, sb.toString(), "Archivos Seleccionados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
