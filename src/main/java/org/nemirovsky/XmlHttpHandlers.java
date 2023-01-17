package org.nemirovsky;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            Document doc = convertStringToXMLDocument(br);
            String response =
                    "<h1>Your XML message received successfully!</h1>";
            if (doc == null) {
                System.out.println("XML is null!");
                response = "<h1>XML is null!!!</h1>";
            } else {
                System.out.println("XML started with " + doc.getFirstChild().getNodeName() + " parsed.");
                addXmlEntry(doc);
            }
            // send response
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }

        private static Document convertStringToXMLDocument(BufferedReader br)
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try
            {
                builder = factory.newDocumentBuilder();
                return builder.parse(new InputSource(br));
            }
            catch (Exception e)
            {
                System.out.println("Exception " + e.getMessage() + " while parsing XML!");
                e.printStackTrace();
            }
            return null;
        }
        private static void addXmlEntry(Document doc) {
            return;
        }
    }

}
