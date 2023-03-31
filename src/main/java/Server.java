import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        Map<String, Integer> purchaseArchive = new HashMap<>();

        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream())) {

                    String request = in.readLine();
                    Purchase purchase = mapper.readValue(request, new TypeReference<>() {
                    });
                    purchase.savePurchase(purchaseArchive);
                    Statistic stat = purchase.getStatistic(purchaseArchive);
                    String response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(stat);
                    out.println(response);
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}
