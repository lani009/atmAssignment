package form;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 서버와 통신하기 위해서 사용하는 소켓.
 */
public class ClientSocket implements Closeable {
    private String ip;
    private String port;
    private Socket client;
    private OutputStream send;
    private InputStream recv;
    private BufferedWriter bw;

    /**
     * ServerProperties.json에서 데이터를 파싱하고, 이를 통해 서버와 연결을 수행한다.
     */
    public ClientSocket() {
        JSONParser parser = new JSONParser();

        // 서버 정보에 대한 데이터를 파싱한다.
        try (FileReader reader = new FileReader("./atm/src/form/ServerProperties.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            this.ip = (String) jsonObject.get("address");
            this.port = (String) jsonObject.get("port");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            client = new Socket();
            InetSocketAddress address = new InetSocketAddress(ip, Integer.parseInt(port));

            client.connect(address);    // 연결 수행

            // 스트림 초기화
            send = client.getOutputStream();
            recv = client.getInputStream();
            bw = new BufferedWriter(new OutputStreamWriter(send));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {

    }



    public String recv() {
        return null;
    }

    public boolean login(String id, String pw) {
        return false;
    }

    /**
     * 클라이언트 커넥션 종료
     */
    @Override
    public void close() throws IOException {
        send("disconnect");
        send.close();
        bw.close();
        recv.close();
        client.close();
    }
}