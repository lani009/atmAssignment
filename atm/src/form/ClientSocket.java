package form;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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

public class ClientSocket {
    private String ip;
    private String port;

    public ClientSocket() {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("./atm/src/form/ServerProperties.json")) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            this.ip = (String) jsonObject.get("address");
            this.port = (String) jsonObject.get("port");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try (Socket client = new Socket()) {
            InetSocketAddress address = new InetSocketAddress(ip, Integer.parseInt(port));
            client.connect(address);
            
            try (OutputStream send = client.getOutputStream(); InputStream recv = client.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(recv));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(send))) {

            }
        } catch (Throwable e) {
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
}