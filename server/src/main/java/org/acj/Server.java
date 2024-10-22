package org.acj;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {
  private static HttpServer server;


  public static void createServer() {
    try {
      Server.server = HttpServer.create(new InetSocketAddress(8000), 0);

      // Define contexts (routes) for GET and POST
      server.createContext("/api/get", new GetHandler());
      server.createContext("/api/post", new PostHandler());
      
      // Start the server with a thread pool (Executor)
      server.setExecutor(null); // default is a single thread
      server.start();
      
      System.out.println("Server started on port 8000...");
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // GET
  private static class GetHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      if ("GET".equals(exchange.getRequestMethod())) {
        String response = "{\"message\":\"GET request received\"}";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
      } else {
        exchange.sendResponseHeaders(405, -1);
      }
    }
  }

  // Handler for POST requests
  private static class PostHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      if ("POST".equals(exchange.getRequestMethod())) {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        
        System.out.println("POST request body: " + requestBody);
        
        String response = "{\"message\":\"POST request received\"}";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
      } else {
        exchange.sendResponseHeaders(405, -1);
      }
    }
  }
}
