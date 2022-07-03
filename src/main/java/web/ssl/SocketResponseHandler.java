package web.ssl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import web.model.Response;

import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class SocketResponseHandler {

    private static final String FINAL_BYTE = "0";

    @SneakyThrows
    public SSLResponse parseResponse(SSLSocket sslSocket, String method) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()))) {
            var respStatus = reader.readLine();
            int statusCode = Integer.parseInt(respStatus.split(" ")[1]);
            var headers = readHeaders(reader);
            SSLResponse response = new SSLResponse();
            response.setStatusCode(statusCode);
            response.setHeaders(headers);

            if (!"HEAD".equals(method)) {
                var body = readBody(reader);
                ObjectMapper mapper = new ObjectMapper();
                var resp = mapper.readValue(body, Response.class);
                response.setPhotos(resp.getPhotos());
            }
            return response;
        }
    }

    @SneakyThrows
    public Map<String, String> readHeaders(BufferedReader reader) {
        String inputLine = null;
        Map<String, String> headers = new HashMap<>();
        while (true) {
            inputLine = reader.readLine();
            if (inputLine.isEmpty()) {
                break;
            }
            var header = inputLine.split(":");
            headers.put(header[0], inputLine.substring(header[0].length() + 1).trim());
        }
        return headers;
    }

    @SneakyThrows
    public String readBody(BufferedReader reader) {
        var count = 0;
        String line = null;
        StringBuilder builder = new StringBuilder();
        while (true) {
            line = reader.readLine();
            if (count++ % 2 == 0) {
                if (FINAL_BYTE.equals(line)) {
                    break;
                }
            } else {
                builder.append(line);
            }

        }

        return builder.toString();
    }
}
