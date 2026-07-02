package ar.edu.utn.frba.dds.http;

import ar.edu.utn.frba.dds.json.JsonParser;
import ar.edu.utn.frba.dds.json.JsonWriter;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtil {

  public static String readBody(HttpExchange exchange) throws IOException {
    try (InputStream is = exchange.getRequestBody();
         ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      byte[] buf = new byte[4096];
      int n;
      while ((n = is.read(buf)) != -1) bos.write(buf, 0, n);
      return bos.toString(StandardCharsets.UTF_8);
    }
  }

  public static Map<String, Object> readJsonBody(HttpExchange exchange) throws IOException {
    String body = readBody(exchange);
    return JsonParser.parseObject(body);
  }

  public static void sendJson(HttpExchange exchange, int statusCode, Object payload) throws IOException {
    String json = JsonWriter.write(payload);
    byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
    exchange.sendResponseHeaders(statusCode, bytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(bytes);
    }
  }
}