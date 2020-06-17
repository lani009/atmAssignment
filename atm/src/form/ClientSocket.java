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
import java.rmi.server.ServerNotActiveException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import form.Enum.BankType;

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
    private BufferedReader br;

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
            br = new BufferedReader(new InputStreamReader(recv));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * String을 전송하기 위한 메소드
     * @param msg
     * @throws IOException
     */
    public void send(String msg) throws IOException {
        bw.write(msg);
        bw.newLine();
        bw.flush();
    }

    /**
     * String을 전달 받기위한 메소드
     * 
     * @return
     * @throws IOException
     * @throws ServerNotActiveException
     */
    public String recv() throws IOException, ServerNotActiveException {
        String line = br.readLine();
        if(line.equals("0") || line.equals("disconnect")) {
            close();    // 0 or disconnect -> socket close
            throw new ServerNotActiveException("Server closed");
        }
        return line;
    }

    /**
     * 로그인 성공여부를 리턴
     * @param id
     * @param pw
     * @return login_success
     */
    public boolean login(String id, String pw, BankType bankType) {
        try {
            send("login");
            send(id);
            send(pw);
            send(bankType.name());
            return br.readLine().equals("login success");
        } catch (Exception e) {
            e.printStackTrace();
        }
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