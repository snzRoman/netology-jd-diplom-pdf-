import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8989;

    public static void main(String[] args) {

        try (Socket clientSocket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ){
            out.println("бизнес");
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonObject = (JSONArray) jsonParser.parse(bufferedReader.readLine());
            System.out.println(jsonObject);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
