package org.nemirovsky;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.nemirovsky.model.Data;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.nemirovsky.XmlHttpServerApp.port;

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

            String response;

            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
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
            System.out.println(xmlData);
            if (xmlData == null) {
                System.out.println("XML is null!");
                response = "<h1>XML is null!!!</h1>";
            } else response = "<h1>XML is successfully parsed!!!</h1>";
            addJsonEntry(xmlData);
            // send response
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }

        private static void addJsonEntry(Data xmlData) throws IOException {
            String jsonData = "";
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            try {
                jsonData = mapper.writeValueAsString(xmlData);
            } catch (Exception e) {
                System.out.println("Exception " + e.getMessage() + " while making JSON!");
                e.printStackTrace();
            }
            System.out.println(jsonData);
            String type = xmlData.getType();
            String filename =
                    Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/output")) + "/" + type +
                            "-" + LocalDate.now() + ".log";
            try {
                File targetFile = new File(filename);
                System.out.println("Creating file result: " + targetFile.createNewFile());
            } catch (Exception e) {
                System.out.println("Exception " + e.getMessage() + " while creating file!");
                e.printStackTrace();
            }
        }
    }

}
