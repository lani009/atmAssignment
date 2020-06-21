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
import form.Enum.BankType;
import server.Enum.RequsetType;

/**
 * 클라이언트와 직접적으로 연결되어 통신하는 클래스.
 * DatabaseDAO 객체와 클라이언트를 연결하는 다리역할을 수행.
 * 
 * @author 정의철
 */
public class ClientConnectionSocket implements Closeable {

    private Socket client;
    private OutputStream send;
    private InputStream recv;
    private BufferedReader recvReader;
    private BufferedWriter sendWriter;
    private String userBank, userId;

    /**
     * Client와 통신할 수 있게 한다. 로그인 실패시 LoginException 발생
     * @param client 클라이언트와 연결된 소켓
     * @throws LoginException 로그인 실패
     */
    public ClientConnectionSocket(Socket client) throws LoginException {
        try {
            this.client = client;
            
            // 스트림 초기화
            send = client.getOutputStream();
            recv = client.getInputStream();
            recvReader = new BufferedReader(new InputStreamReader(recv));
            sendWriter = new BufferedWriter(new OutputStreamWriter(send));

            if(!login()) {
                send("login failed");
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
     * 클라이언트에게 문자열 전송
     * @param msg 문자열
     */
    public void send(String msg) {
        try {
            sendWriter.write(msg);
            sendWriter.newLine();
            sendWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 클라이언트로 부터 온 메시지 리턴
     * @return message 클라이언트로 부터 받은 메시지
     * @throws IOException BufferedReader 예외
     */
    public String recv() throws IOException {
        return recvReader.readLine();
    }

    public Transaction recvTransaction() {
        return null;
    }

    /**
     * Authentication 객체와 통신하여 로그인 성공, 실패 여부를 리턴
     * @return 로그인 성공 여부
     */
    private boolean login() throws IOException {
        return Authentication.getInstance().login(userId = recvReader.readLine(), recvReader.readLine(), userBank = recvReader.readLine());
    }

    /**
     * 해당 유저가 사용하는 은행의 종류 반환
     * @return bankType 유저의 은행 종류
     */
    public BankType getUserBankType() {
        return BankType.valueOf(userBank);
    }

    /**
     * 해당 유저의 ID 반환
     * @return id 연결된 유저의 ID
     */
    public String getUserId() {
        return this.userId;
    }

    /**
     * 클라이언트로 부터 Request를 받아 리턴
     * @return Request 클라이언트 요청
     */
    public RequsetType getPhase() {
        try {
            String input = recv();
            if(input.equals("transaction")) {
                return RequsetType.TRANSACTION;
            } else if(input.equals("get my account list")) {
                return RequsetType.GETMYACCOUNTLIST;
            } else if(input.equals("get my transaction list")) {
                return RequsetType.GETMYTRANSACTIONLIST;
            } else if(input.equals("search account")) {
                return RequsetType.SEARCHACCOUNT;
            } else if(input.equals("0") || input.equals("disconnect")) {
                return RequsetType.DISCONNECT;
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