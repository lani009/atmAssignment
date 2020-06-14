package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.security.auth.login.LoginException;

import form.Transaction;
import server.Enum.DAOType;

/**
 * 클라이언트와 직접적으로 연결되어 통신하는 클래스
 */
public class ClientConnectionSocket implements Closeable {

    private Socket client;
    private OutputStream send;
    private InputStream recv;
    private BufferedReader recvReader;
    private BufferedWriter sendWriter;

    /**
     * Client와 통신할 수 있게 한다. 로그인 실패시 LoginException 발생
     * @param client
     * @throws LoginException
     */
    public ClientConnectionSocket(Socket client) throws LoginException {
        try {
            this.client = client;
            
            // 스트림 초기화
            send = client.getOutputStream();
            recv = client.getInputStream();
            recvReader = new BufferedReader(new InputStreamReader(recv));
            sendWriter = new BufferedWriter(new OutputStreamWriter(send));
            sendWriter.write("server hello");
            sendWriter.flush();

            if(!login()) {
                throw new LoginException("User Failed login");
            } else {
                send("login success");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.printf("Client disconnected. IP: %s \n", client.getRemoteSocketAddress().toString());
        }
    }

    /**
     * 문자열 전송
     * @param 문자열
     */
    public void send(String msg) {
        try {
            sendWriter.write(msg);
            sendWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String recv() throws IOException {
        return recvReader.readLine();
    }

    public Transaction recvTransaction() {
        return null;
    }

    /**
     * Authentication 객체와 통신하여 로그인 성공, 실패 여부를 리턴
    */
    private boolean login() throws IOException {
        return Authentication.getInstance().login(recvReader.readLine(), recvReader.readLine(), recvReader.readLine());
    }

    public DAOType getPhase() {
        try {
            String input = recv();
            if(input.equals("transaction")) {

            } else if(input.equals("get transaction list")) {

            } else if(input.equals("anObject")) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 통신 해제
     */
    @Override
    public void close() throws IOException {
        send("disconnect");
        send.close();
        sendWriter.close();
        recv.close();
        recvReader.close();
        client.close();
    }
}