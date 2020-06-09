package server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket()) {
            InetSocketAddress port = new InetSocketAddress(9999);
            server.bind(port);

            System.out.printf("Server Init. %d port bind.\n", port.toString());

            ExecutorService receiver = Executors.newCachedThreadPool();


            while (true) {
                try {
                    Socket client = server.accept();
                    System.out.printf("Client Connected. IP: %s\n", client.getRemoteSocketAddress().toString());
                    receiver.execute(new DatabaseDAO(client));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
