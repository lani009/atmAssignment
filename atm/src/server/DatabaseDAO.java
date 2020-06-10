package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class DatabaseDAO implements Runnable {
    private Socket client;

    public DatabaseDAO(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (Socket thisClient = client;
                OutputStream send = client.getOutputStream();
                InputStream recv = client.getInputStream();
                BufferedReader recvReader = new BufferedReader(new InputStreamReader(recv));
                BufferedWriter sendWriter = new BufferedWriter(new OutputStreamWriter(send))) {
            
            while (true) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.printf(
                    "Client disconnected. IP: %s \n", client.getRemoteSocketAddress().toString());
        }

    }

}