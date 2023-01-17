package org.nemirovsky;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class XmlHttpServer {
    private int port;
    private int poolSize;
    private HttpServer server;

    public void start(int port, int poolSize) {
        try {
            this.port = port;
            this.poolSize = poolSize;
            server = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("XmlHttpServer server started at " + port);
            server.createContext("/", new XmlHttpHandlers.RootHandler());
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            server.setExecutor(threadPoolExecutor);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        server.stop(0);
        System.out.println("XmlHttpServer stopped!");
    }
}
