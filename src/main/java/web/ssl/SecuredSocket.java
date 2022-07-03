package web.ssl;

import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import web.model.Photo;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.util.Comparator;

public class SecuredSocket {

    private static final String URL = "api.nasa.gov";

    private static final String REQUEST_PATH = "/mars-photos/api/v1/rovers/curiosity/photos?sol=16&api_key=DEMO_KEY";

    private static final int SSL_PORT = 443;

    @SneakyThrows
    public static void main(String[] args) {
        SocketResponseHandler responseHandler = new SocketResponseHandler();

        try (SSLSocket sslSocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(URL, SSL_PORT)) {
            sslSocket.startHandshake();
            var method = "GET";
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream())))) {
                writer.println(String.format("%s %s HTTP/1.1", method, REQUEST_PATH));
                writer.println(String.format("Host: %s", URL));
                writer.println();
                writer.flush();

                if (writer.checkError()) {
                    System.out.println("SSLSocketClient:  java.io.PrintWriter error");
                }

                var response = responseHandler.parseResponse(sslSocket, method);

                var maxSizeImg = response.getPhotos().parallelStream()
                        .map(photo -> getImgRedirectUri(photo, responseHandler))
                        .map(imgLocation -> getImgSize(imgLocation, responseHandler))
                        .max(Comparator.comparing(Pair::getValue));
                System.out.println(maxSizeImg.orElseThrow().getKey());
                System.out.println(maxSizeImg.orElseThrow().getValue());
            }
        }
    }

    @SneakyThrows
    private static Pair<String, URI> getImgRedirectUri(Photo photo, SocketResponseHandler responseHandler) {
        var url = URI.create(photo.getImageSource());
        var method = "HEAD";
        try (SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(url.getHost(), SSL_PORT)) {
            socket.startHandshake();
            PrintWriter headWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())));
            headWriter.println(String.format("%s %s HTTP/1.1", method, url.getPath()));
            headWriter.println(String.format("Host: %s", url.getHost()));
            headWriter.println();
            headWriter.flush();

            var headResp = responseHandler.parseResponse(socket, method);
            return Pair.of(photo.getImageSource(), URI.create(headResp.getHeaders().get("Location")));
        }
    }

    @SneakyThrows
    private static Pair<String, Integer> getImgSize(Pair<String, URI> imgLocation, SocketResponseHandler responseHandler) {
        var method = "HEAD";
        try (SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(imgLocation.getValue().getHost(), SSL_PORT)) {
            socket.startHandshake();
            PrintWriter headWriter = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())));
            headWriter.println(String.format("%s %s HTTP/1.1", method, imgLocation.getValue().getPath()));
            headWriter.println(String.format("Host: %s", imgLocation.getValue().getHost()));
            headWriter.println();
            headWriter.flush();
            var headResp = responseHandler.parseResponse(socket, method);
            return Pair.of(imgLocation.getKey(),
                    Integer.parseInt(headResp.getHeaders().get("Content-Length")));
        }
    }

}


