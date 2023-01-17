package org.nemirovsky;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.nemirovsky.XmlHttpServerApp.poolSize;
import static org.nemirovsky.XmlHttpServerApp.port;

public class XmlHttpServerTests {

    static final XmlHttpServer httpServer = new XmlHttpServer();

    @BeforeAll
    static void setUp() {
        httpServer.start(port, poolSize);
    }

    @Test
    @DisplayName("Check if server is up")
    void testServerIsUp() {
        assertTrue(checkServer());
    }

    @AfterAll
    static void setDown() {
        httpServer.stop();
    }

    private boolean checkServer() {
        boolean result = true;
        try {
            URL url = new URL("http://localhost:" + port + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestMethod("GET");
            System.out.println(connection.getResponseMessage());
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                result = false;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
}
