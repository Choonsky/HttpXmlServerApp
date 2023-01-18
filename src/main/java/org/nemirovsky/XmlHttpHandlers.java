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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

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

            Path targetFile = Paths.get(targetDirectory + type + "-" + LocalDate.now() + ".log");
            try {
                Files.createDirectories(targetFile.getParent()); // create if not exists yet
                if (!Files.exists(targetFile))
                    Files.createFile(targetFile); // create if not exists yet
            } catch (Exception e) {
                System.out.println("Exception " + e.getMessage() + " while creating file/directory!");
                e.printStackTrace();
            }

            if (Files.size(targetFile) == 0) {
                try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {
                    writer.write("1\n");
                } catch (IOException e) {
                    System.out.println("Exception " + e.getMessage() + " while writing to file!");
                    e.printStackTrace();
                }
            }

            Scanner reader = new Scanner(targetFile);
            System.out.println(reader.nextLine());
            reader.close();
        }
    }

}
