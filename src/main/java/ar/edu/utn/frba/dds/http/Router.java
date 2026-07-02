package ar.edu.utn.frba.dds.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router implements HttpHandler {

  private static class Route {
    final String method;
    final Pattern pattern;
    final List<String> paramNames;
    final RouteHandler handler;

    Route(String method, Pattern pattern, List<String> paramNames, RouteHandler handler) {
      this.method = method;
      this.pattern = pattern;
      this.paramNames = paramNames;
      this.handler = handler;
    }
  }

  private final List<Route> routes = new ArrayList<>();

  public void register(String method, String pathTemplate, RouteHandler handler) {
    List<String> paramNames = new ArrayList<>();
    StringBuilder regex = new StringBuilder("^");
    for (String segment : pathTemplate.split("/")) {
      if (segment.isEmpty()) continue;
      regex.append("/");
      if (segment.startsWith("{") && segment.endsWith("}")) {
        paramNames.add(segment.substring(1, segment.length() - 1));
        regex.append("([^/]+)");
      } else {
        regex.append(Pattern.quote(segment));
      }
    }
    regex.append("/?$");
    routes.add(new Route(method.toUpperCase(), Pattern.compile(regex.toString()), paramNames, handler));
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String method = exchange.getRequestMethod();
    String path = exchange.getRequestURI().getPath();

    for (Route route : routes) {
      if (!route.method.equals(method)) continue;
      Matcher matcher = route.pattern.matcher(path);
      if (matcher.matches()) {
        Map<String, String> params = new LinkedHashMap<>();
        for (int i = 0; i < route.paramNames.size(); i++) {
          params.put(route.paramNames.get(i), matcher.group(i + 1));
        }
        dispatch(route.handler, exchange, params);
        return;
      }
    }
    HttpUtil.sendError(exchange, 404, "Recurso no encontrado: " + method + " " + path);
  }

  private void dispatch(RouteHandler handler, HttpExchange exchange, Map<String, String> params) throws IOException {
    try {
      handler.handle(exchange, params);
    } catch (RuntimeException e) {
      HttpUtil.sendError(exchange, 400, e.getMessage());
    } catch (Exception e) {
      HttpUtil.sendError(exchange, 500, "Error interno: " + e.getMessage());
    } finally {
      exchange.close();
    }
  }
}