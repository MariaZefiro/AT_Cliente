package org.example;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import  java.awt.*;
import java.nio.charset.StandardCharsets;
public class Main {
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Cliente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));
        JLabel label1 = new JLabel("Id:");
        label1.setFont((new Font("Arial", Font.BOLD, 30)));
        panel.add(label1);
        JTextField inputField1 = new JTextField();
        inputField1.setFont((new Font("Arial", Font.PLAIN, 30)));
        panel.add(inputField1);
        JLabel label2 = new JLabel("Response:");
        label2.setFont((new Font("Arial", Font.BOLD, 30)));
        panel.add(label2);
        JTextField inputField2 = new JTextField();
        inputField2.setFont((new Font("Arial", Font.PLAIN, 30)));
        panel.add(inputField2);
        inputField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String id = inputField1.getText();
                boolean changeColor = enviarRequest(id);
                if(inputField1.getText().length() == 10) {
                    if (changeColor) {
                        Thread fio = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    inputField2.setBackground(Color.GREEN);
                                    Thread.sleep(3000);
                                    inputField2.setBackground(Color.WHITE);
                                    inputField1.setText("");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        fio.start();
                    } else {
                        Thread fio = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    inputField2.setBackground(Color.RED);
                                    Thread.sleep(3000);
                                    inputField2.setBackground(Color.WHITE);
                                    inputField1.setText("");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        fio.start();
                    }
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {}
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
        frame.add(panel);
        frame.setVisible(true);
    }
    public static boolean enviarRequest(String id){
        boolean changeColor = false;
        String CardId = id;
        try {
            String url = "http://localhost:8080/verificarCartao";
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            String jsonInputString = "{\"id\":\"" + CardId + "\"}";
            httpPost.setEntity(new StringEntity(jsonInputString, StandardCharsets.UTF_8));
            HttpResponse response = httpClient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
            changeColor = processing(responseBody.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return changeColor;
    }
    private static boolean processing(String resposta) {
        if (resposta != null && resposta.contains("1"))
            return false;
        return true;
    }
}