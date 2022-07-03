package web.ssl;

import lombok.Data;
import web.model.Response;

import java.util.Map;

@Data
public class SSLResponse extends Response {

    private int statusCode;

    private Map<String, String> headers;

}
