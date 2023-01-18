# Drone dispatcher service

## Table of contents

* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info

This project is a sample HTTP server that accepts POST requests with XML content,
parses XML to JSON format and saves it in log files according to Type field
including count.

## Technologies

Project is created with:

* Java 19 (Oracle OpenJDK)
* Maven 3.8.1
* Lombok 1.18.24
* FasterXML Jackson 2.13/2.14
* JUnit 5.9.1

## Setup

To test this project locally:

* Clone this project using "git clone https://github.com/Choonsky/HttpXmlServerApp"
* Navigate to the program folder ("cd HttpXmlServerApp") 
* Make a jar using "mvn package"
* Run a jar using "java -cp target/HttpXmlServerApp-1.0-SNAPSHOT.jar org.nemirovsky.XmlHttpServerApp"

* Open "http://localhost:9091" in your browser to test that a server is working
* Post a request with XML as a body using Postman or other tool to "http://localhost:9091/xml"
* Enjoy