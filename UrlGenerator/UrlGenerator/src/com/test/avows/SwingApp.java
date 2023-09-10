package com.test.avows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SwingApp {
    private JFrame frame;
    private JTextField urlPathTextField;
    private JTextField urlPathTextField2;
    private JTextArea requestTextArea;
    private JTextArea responseTextArea;
    private JComboBox<String> requestDropdown;
    private JPanel panel1;

    public SwingApp() {
        frame = new JFrame("HTTP Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JLabel urlLabel = new JLabel("IP");
        urlPathTextField = new JTextField(30);
        urlPathTextField2 = new JTextField(30);
        urlPathTextField.setText("http://127.0.0.1:8080");
        urlPathTextField2.setText("/qr/payment");
        topPanel.add(urlLabel);
        topPanel.add(urlPathTextField);
        topPanel.add(urlPathTextField2);

        JLabel requestLabel = new JLabel("Request");
        requestTextArea = new JTextArea(10, 40);
        requestTextArea.setText("{\n" + "  \"pan\":\"0099888999\",\n" + "  \"processingCode\":\"000111\",\n"
                + "  \"transactionAmount\":123000.00\n" + "}");
        JScrollPane requestScrollPane = new JScrollPane(requestTextArea);
        topPanel.add(requestLabel);
        topPanel.add(requestScrollPane);

        JLabel responseLabel = new JLabel("Response");
        responseTextArea = new JTextArea(10, 40);
        responseTextArea.setEditable(false);
        JScrollPane responseScrollPane = new JScrollPane(responseTextArea);
        topPanel.add(responseLabel);
        topPanel.add(responseScrollPane);

        frame.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        JLabel requestDropdownLabel = new JLabel("List of Request");
        String[] requestItems = {"Payment", "Check", "Refund", "Inquiry", "Payment Debit", "Payment Reversal",
                "Payment Reversal Rep", "TCICO Inquiry"};
        requestDropdown = new JComboBox<>(requestItems);
        bottomPanel.add(requestDropdownLabel);
        bottomPanel.add(requestDropdown);

        JButton kirimButton = new JButton("Kirim");
        JButton hapusButton = new JButton("Hapus");
        JButton trxBaruButton = new JButton("Trx Baru");
        bottomPanel.add(kirimButton);
        bottomPanel.add(hapusButton);
        bottomPanel.add(trxBaruButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        requestDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // You can implement logic to change the URL and Request text based on the selected item.
                String selectedRequest = (String) requestDropdown.getSelectedItem();
                if ("Payment".equals(selectedRequest)) {
                    urlPathTextField.setText("URL 1");
                    requestTextArea.setText("Request Data 1");
                } else if ("Check".equals(selectedRequest)) {
                    urlPathTextField.setText("URL 2");
                    requestTextArea.setText("Check");
                }
            }
        });

        kirimButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // set url path
                String url = urlPathTextField.getText() + urlPathTextField2.getText();
                String request = requestTextArea.getText();

                // Send the request and get the response.
                String response = null;
                try {
                    response = sendRequest(request, url);
                    responseTextArea.setText(response);
                } catch (IOException ex) {
                    responseTextArea.setText(ex.getMessage());
                }
            }
        });

        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                responseTextArea.setText("");
            }
        });

        frame.setVisible(true);
    }

    private String sendRequest(String request, String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        // For POST only - START
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(request.getBytes(StandardCharsets.UTF_8));
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();

            return response.toString();
        } else {
            return "POST request not worked";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SwingApp();
            }
        });
    }
}
