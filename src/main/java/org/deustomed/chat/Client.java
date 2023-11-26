package org.deustomed.chat;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);

            // Configurar flujos de entrada/salida
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Crear un hilo para leer mensajes del servidor
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        String receivedMessage = in.readLine();
                        if (receivedMessage == null || receivedMessage.equals("bye")) {
                            break;
                        }
                        System.out.println("Servidor: " + receivedMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readThread.start();

            // Hilo principal para enviar mensajes al servidor
            try {
                while (true) {
                    // Leer mensaje desde la consola y enviar al servidor
                    System.out.print("Escribe un mensaje para el servidor: ");
                    BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
                    String sendMessage = consoleInput.readLine();
                    out.println(sendMessage);

                    if (sendMessage.equals("bye")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Cerrar conexiones
            readThread.join(); // Esperar a que el hilo de lectura termine
            in.close();
            out.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

