package org.nemirovsky;

public class XmlHttpServerApp {

    public static int port = 9091;
    public static int poolSize = 10;
    public static String targetDirectory = System.getProperty("user.dir") + "/output/";

    public static void main(String[] args) {

		XmlHttpServer httpServer = new XmlHttpServer();
		httpServer.start(port, poolSize);

		System.out.println("Files will be saved to " + targetDirectory);

    }
}