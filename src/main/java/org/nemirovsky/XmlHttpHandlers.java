package org.nemirovsky;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.nemirovsky.model.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.nemirovsky.XmlHttpServerApp.port;
import static org.nemirovsky.XmlHttpServerApp.targetDirectory;

public class XmlHttpHandlers {
    public static class RootHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response =
                    "<h1>Server started successfully</h1>" + "<h1>Port: " + port + "</h1>";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }

    public static class XmlHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String response = "<h1>XML server testing</h1>";

            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
            if (!"POST".equals(httpExchange.getRequestMethod())) {
                response = "<h1>Only POST requests are allowed!</h1>";
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, response.length());
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
                return;
            }
            if (!"application/xml".equals(httpExchange.getRequestHeaders().get("Content-Type").get(0))) {
                response = "<h1>Only application/xml type content is allowed!</h1>";
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_ACCEPTABLE, response.length());
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
                return;
            }

            if (!httpExchange.getRequestHeaders().containsKey("Testing")) {
                BufferedReader br = new BufferedReader(isr);
                XmlMapper xmlMapper = new XmlMapper();
                xmlMapper.findAndRegisterModules();
                Data xmlData = new Data();
                try {
                    xmlData = xmlMapper.readValue(br, Data.class);
                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage() + " while parsing XML!");
                    e.printStackTrace();
                }
                if (xmlData == null) {
                    System.out.println("XML is null!");
                    response = "<h1>XML is null!!!</h1>";
                } else response = "<h1>XML is successfully parsed!!!</h1>";
                addJsonEntry(xmlData);
            }
            // send response
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }

        private static void addJsonEntry(Data xmlData) {
            String jsonData = "";
            ObjectMapper mapper = new ObjectMapper();
            // Mandatory for Dates mapping
            mapper.findAndRegisterModules();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            try {
                jsonData = mapper.writeValueAsString(xmlData);
            } catch (Exception e) {
                System.out.println("Exception " + e.getMessage() + " while making JSON!");
                e.printStackTrace();
            }
            String type = xmlData.getType();

            Path targetFile = Paths.get(targetDirectory + type + "-" + LocalDate.now() + ".log");

            // locating/creating file and directory
            try {
                Files.createDirectories(targetFile.getParent()); // create if not exists yet
                if (!Files.exists(targetFile))
                    Files.createFile(targetFile); // create if not exists yet
            } catch (Exception e) {
                System.out.println("Exception " + e.getMessage() + " while creating file/directory!");
                e.printStackTrace();
            }

            // reading all lines and changing 1st line (cannot find a way to do it without reading all file)
            try {
                List<String> fileContent = new ArrayList<>(Files.readAllLines(targetFile, StandardCharsets.UTF_8));
                if (fileContent.size() == 0) {
                    fileContent.add(String.valueOf(1));
                } else {
                    fileContent.set(0, String.valueOf(Integer.parseInt(fileContent.get(0)) + 1));
                }
                fileContent.add(jsonData);
                Files.write(targetFile, fileContent, StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.out.println("Exception " + e.getMessage() + " while writing to file!");
                e.printStackTrace();
            }
        }
    }

}
