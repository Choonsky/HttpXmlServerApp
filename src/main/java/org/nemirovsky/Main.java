package org.nemirovsky;

public class Main {

    public static int serverPort = 9091;
    public static int poolSize = 10;

    public static void main(String[] args) {

		XmlHttpServer httpServer = new XmlHttpServer();
		httpServer.start(serverPort, poolSize);

		System.out.println("Files will be saved at " + System.getProperty("user.dir") + "\\output");

    }
}