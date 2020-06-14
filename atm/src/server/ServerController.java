package server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

/**
 * Client로 부터 받아온 정보를 토대로, DAO와 통신한다.
 */
public class ServerController {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket()) {
            InetSocketAddress port = new InetSocketAddress(9999);
            server.bind(port);

            System.out.printf("Server Init. %d port bind.\n", port.toString());

            // Thread Pool 생성
            ExecutorService receiver = Executors.newCachedThreadPool();

            while (true) {
                try {
                    Socket client = server.accept();    // client Accept
                    System.out.printf("Client Connected. IP: %s\n", client.getRemoteSocketAddress().toString());
                    client.setKeepAlive(true);  // TCP Kepp-Alive = true

                    // Thread 실행
                    receiver.execute(new DatabaseDAO(new ClientConnectionSocket(client)));
                } catch (LoginException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
