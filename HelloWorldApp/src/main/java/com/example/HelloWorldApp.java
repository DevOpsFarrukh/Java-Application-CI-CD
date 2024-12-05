package com.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class HelloWorldApp {
    public static void main(String[] args) throws Exception {
        // Create an HTTP server that listens on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Define the context and response for the root URL "/"
        server.createContext("/", exchange -> {
            String response = "<html lang=\"en\">" + 
                        "<head>" + 
                        "    <meta charset=\"UTF-8\">" + 
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" + 
                        "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">" + 
                        "    <title>Syed Umair Ali Gilani</title>" + 
                        "    <style>" + 
                        "        .fa { padding: 20px; font-size: 30px; width: 50px; text-align: center; text-decoration: none; margin: 5px 10px; display: inline-block; }" + 
                        "        .fa:hover { opacity: 0.7; }" + 
                        "        .fa-linkedin { background: #007bb5; color: white; }" + 
                        "        .fa-google { background: #dd4b39; color: white; }" + 
                        "        .fa-facebook { background: #3B5998; color: white; }" + 
                        "        body { background-color: white; display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; margin: 0; font-family: Arial, sans-serif; }" + 
                        "        h1 { color: black; font-size: 48px; font-family: 'Comic Sans MS', cursive, sans-serif; }" + 
                        "        h2 { color: black; font-size: 24px; font-family: 'Comic Sans MS', cursive, sans-serif; }" + 
                        "        button { padding: 10px 20px; font-size: 18px; margin-top: 20px; cursor: pointer; }" + 
                        "        button:hover { animation: shake 0.5s; animation-iteration-count: infinite; }" + 
                        "        @keyframes shake { " + 
                        "            0% { transform: translate(1px, 1px) rotate(0deg); }" + 
                        "            10% { transform: translate(-1px, -2px) rotate(-1deg); }" + 
                        "            20% { transform: translate(-3px, 0px) rotate(1deg); }" + 
                        "            30% { transform: translate(3px, 2px) rotate(0deg); }" + 
                        "            40% { transform: translate(1px, -1px) rotate(1deg); }" + 
                        "            50% { transform: translate(-1px, 2px) rotate(-1deg); }" + 
                        "            60% { transform: translate(-3px, 1px) rotate(0deg); }" + 
                        "            70% { transform: translate(3px, 1px) rotate(-1deg); }" + 
                        "            80% { transform: translate(-1px, -1px) rotate(1deg); }" + 
                        "            90% { transform: translate(1px, 2px) rotate(0deg); }" + 
                        "            100% { transform: translate(1px, -2px) rotate(-1deg); }" + 
                        "        }" + 
                        "    </style>" + 
                        "</head>" + 
                        "<body>" + 
                        "    <h1>My Name is 'Syed Umair Ali Gilani'</h1>" + 
                        "    <div>" + 
                        "        <a href=\"#\" class=\"fa fa-linkedin\"></a>" + 
                        "        <a href=\"#\" class=\"fa fa-facebook\"></a>" + 
                        "        <a href=\"#\" class=\"fa fa-google\"></a>" + 
                        "    </div>" + 
                        "    <h2>This is my Java Application</h2>" + 
                        "    <button onclick=\"window.location.href='/image'\">Click Me</button>" + 
                        "</body>" + 
                        "</html>";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        // Define a context to serve the image
        server.createContext("/image", new ImageHandler());

        // Start the server
        server.start();
        System.out.println("Server is listening on port 8080...");

        // Keep the application running indefinitely
        Thread.currentThread().join();
    }

    // Handler to serve the image
    static class ImageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File("src/main/resources/static/umair.png"); // Adjust path if necessary

            if (!file.exists()) {
                String response = "Image not found.";
                exchange.sendResponseHeaders(404, response.length());
                exchange.getResponseBody().write(response.getBytes());
            } else {
                exchange.sendResponseHeaders(200, file.length());
                try (FileInputStream fis = new FileInputStream(file);
                     OutputStream os = exchange.getResponseBody()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            }
            exchange.close();
        }
    }
}
