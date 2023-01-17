package org.nemirovsky;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Hello world!");
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 9091), 0);
        server.createContext("/test", new MyHttpHandler());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.setExecutor(threadPoolExecutor);
        server.start();
        log.info("Test...");
    }
}