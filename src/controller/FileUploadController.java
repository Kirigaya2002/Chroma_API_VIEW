package controller;

import model.FileUploadModel;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class FileUploadController {

    private final FileUploadModel model;
    private final JFrame parentFrame;

    public FileUploadController(FileUploadModel model, JFrame parentFrame) {
        this.model = model;
        this.parentFrame = parentFrame;
    }

    public void uploadFiles() {
        if (!model.hasFiles()) {
            JOptionPane.showMessageDialog(parentFrame, "No hay archivos seleccionados.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @SuppressWarnings({ "resource", "deprecation" })
			@Override
            protected Void doInBackground() {
                try {
                    String boundary = "===" + System.currentTimeMillis() + "===";
                    URL url = new URL("http://127.0.0.1:8000/process-docs/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    try (DataOutputStream request = new DataOutputStream(connection.getOutputStream())) {
                        for (File file : model.getSelectedFiles()) {
                            writeFilePart(request, file, boundary);
                        }
                        request.writeBytes("--" + boundary + "--\r\n");
                        request.flush();
                    }

                    int responseCode = connection.getResponseCode();
                    String response = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                            .lines().reduce("", (acc, line) -> acc + line + "\n");

                    JOptionPane.showMessageDialog(parentFrame,
                            "Respuesta del servidor (" + responseCode + "):\n" + response,
                            "Subida completa", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Error al subir archivos: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }
        };

        worker.execute();
    }

    private void writeFilePart(DataOutputStream request, File file, String boundary) throws IOException {
        String fileName = file.getName();
        request.writeBytes("--" + boundary + "\r\n");
        request.writeBytes("Content-Disposition: form-data; name=\"files\"; filename=\"" + fileName + "\"\r\n");
        request.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(fileName) + "\r\n\r\n");

        try (FileInputStream input = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                request.write(buffer, 0, bytesRead);
            }
        }

        request.writeBytes("\r\n");
    }
}
