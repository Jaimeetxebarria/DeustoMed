package org.deustomed.chat;

import org.deustomed.ui.WindowPatient;

import java.io.*;
import java.net.*;

public class Server {
    protected WindowPatient windowPatientt;
    public void initServer(WindowPatient windowPatient){
        windowPatientt = windowPatient;
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            //Socket clientSocket = serverSocket.accept();

            Thread readThread = new Thread(() -> {
                while (true) {
                    String receivedMessage = windowPatient.getLastMessage();
                    System.out.println("Cliente: " + receivedMessage);
                }
            });
            readThread.start();

            try {
                while (true) {
                    System.out.print("Escribe un mensaje para el cliente: ");
                    BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
                    String sendMessage = consoleInput.readLine();
                    windowPatient.recieveMessage(sendMessage);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            readThread.join();
            //clientSocket.close();
            serverSocket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    }

