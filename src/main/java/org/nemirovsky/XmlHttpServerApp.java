package org.nemirovsky;

import java.io.File;

public class XmlHttpServerApp {

    public static int port = 9091;
    public static int testingPort = 9092;
    public static int poolSize = 10;
    public static String targetDirectory = System.getProperty("user.home") + File.separator + "output";

    public static void main(String[] args) {

		XmlHttpServer httpServer = new XmlHttpServer();
		httpServer.start(port, poolSize);

		System.out.println("Files will be saved to " + targetDirectory);

    }
}