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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import form.Enum.BankType;

/**
 * 서버와 직접적으로 통신을 수행한다. TransactionDAO와 서버 사이를 연결하는 다리역할.
 * 
 * @author 정의철
 */
public class ClientSocket implements Closeable {
    private String ip;
    private String port;
    private String userId, userPw;
    private BankType userBankType;
    private Socket client;
    private OutputStream send;
    private InputStream recv;
    private BufferedWriter bw;
    private BufferedReader br;

    /**
     * ServerProperties.json에서 서버 주소를 불러와서 소켓 연결을 수행한다.
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
            client.setKeepAlive(true);
            client.connect(address);    // 연결 수행

            // IO 스트림 초기화
            send = client.getOutputStream();
            recv = client.getInputStream();
            bw = new BufferedWriter(new OutputStreamWriter(send));
            br = new BufferedReader(new InputStreamReader(recv));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 서버에 String을 전송하기 위한 메소드
     * @param msg String메시지
     * @throws IOException 서버 연결 강제 종료
     */
    public void send(String msg) {
        try {
            bw.write(msg);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 서버로 부터 String을 전달 받기위한 메소드
     * 
     * @return msg 서버가 전송한 메시지
     * @throws IOException 서버 연결 강제 종료
     */
    public String recv() throws IOException {
        String line = br.readLine();
        if(line.equals("0") || line.equals("disconnect")) {
            // 0 또는 disconnect를 받았을 경우, 서버가 종료된 상태.
            login(userId, userPw, userBankType);
        }
        return line;
    }

    /**
     * 서버에 로그인을 요청한다. 로그인 성공 여부를 리턴.
     * @param id 고객 아이디
     * @param pw 고객 비밀번호
     * @param bankType 고객 은행 종류
     * @return isLoggedIn 로그인 성공 여부
     */
    public boolean login(String id, String pw, BankType bankType) {
        try {
            send(id);
            send(pw);
            send(bankType.name());
            this.userId = id;
            this.userPw = pw;
            this.userBankType = bankType;
            return br.readLine().equals("login success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 서버와의 연결 종료
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