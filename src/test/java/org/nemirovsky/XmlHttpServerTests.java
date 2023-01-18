package org.nemirovsky;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.nemirovsky.XmlHttpServerApp.poolSize;
import static org.nemirovsky.XmlHttpServerApp.testingPort;

public class XmlHttpServerTests {

    static final XmlHttpServer httpServer = new XmlHttpServer();

    @BeforeAll
    static void setUp() {
        httpServer.start(testingPort, poolSize);
    }

    @Test
    @DisplayName("Check that server is up")
    void testServerIsUp() throws IOException {
        String url = "http://localhost:" + testingPort + "/";
        String requestMethod = "GET";
        String contentType = "application/xml";
        assertEquals(HttpURLConnection.HTTP_OK, checkServer(url, requestMethod, contentType));
    }

    @Test
    @DisplayName("Check that server is not accepting GET requests")
    void testServerIsRejectingGetRequests() throws IOException {
        String url = "http://localhost:" + testingPort + "/xml";
        String requestMethod = "GET";
        String contentType = "application/xml";
        assertEquals(HttpURLConnection.HTTP_BAD_METHOD, checkServer(url, requestMethod, contentType));
    }

    @Test
    @DisplayName("Check that server is not accepting wrong content-type")
    void testServerIsRejectingWrongContentType() throws IOException {
        String url = "http://localhost:" + testingPort + "/xml";
        String requestMethod = "POST";
        String contentType = "text/html";
        assertEquals(HttpURLConnection.HTTP_NOT_ACCEPTABLE, checkServer(url, requestMethod, contentType));
    }

    @Test
    @DisplayName("Check that server is accepting right content-type")
    void testServerIsAcceptingRightContentType() throws IOException {
        String url = "http://localhost:" + testingPort + "/xml";
        String requestMethod = "POST";
        String contentType = "application/xml";
        assertEquals(HttpURLConnection.HTTP_OK, checkServer(url, requestMethod, contentType));
    }
    @AfterAll
    static void setDown() {
        httpServer.stop();
    }

    private int checkServer(String url, String requestMethod, String contentType) throws IOException {
        HttpURLConnection connection;
        int response;
        try {
            URL address = new URL(url);
            connection = (HttpURLConnection) address.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Testing", "yes");
            System.out.println(connection.getResponseMessage());
        } catch (Exception e) {
            return HttpURLConnection.HTTP_INTERNAL_ERROR;
        }
        response = connection.getResponseCode();
        connection.disconnect();
        return response;
    }
}
