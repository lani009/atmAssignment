package project3.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.login.LoginException;

/**
 * 은행의 서버.
 * 클라이언트와의 연결을 수립한 이후, 스레드를 생성하여 DatabaseDAO에게 소켓을 넘겨준다.
 * 
 * @author 정의철
 */
public class ServerController {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket()) {
            InetSocketAddress port = new InetSocketAddress(9999);
            server.bind(port);

            System.out.printf("Server Init. %s port bind.\n", port.toString());

            // Thread Pool 생성, 스레드 개수 10개
            ExecutorService receiver = Executors.newFixedThreadPool(10);
            while (true) {
                Socket client = null;
                try {
                    client = server.accept();    // client Accept
                    System.out.printf("##** Client Connected. IP: %s **##\n", client.getRemoteSocketAddress().toString());
                    client.setKeepAlive(true);  // TCP Kepp-Alive = true

                    // Thread 실행
                    receiver.execute(new DatabaseDAO(new ClientConnectionSocket(client)));
                } catch (LoginException e) {
                    client.close(); // 로그인 실패시 소켓 연결 close
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
