package org.nemirovsky;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static int serverPort = 9091;
    public static int poolSize = 10;

    public static void main(String[] args) throws IOException {

        // start http server
		XmlHttpServer httpServer = new XmlHttpServer();
		httpServer.start(serverPort, poolSize);

		System.out.println("Files will be saved at " + System.getProperty("user.dir"));

    }
}