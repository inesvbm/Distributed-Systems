# ucBusca - Web browser
Web browser based on a Client-Server architecture. It allows automatic indexing (Web crawler) and search (search engine). The communication is carried out through UDP and Multicast sockets and the access to the client data is made through Java RMI. Jsoup is used to extract iinformation from Web pages. 

## Prerequisites
- Java Development Kit (JDK) - https://www.oracle.com/java/technologies/javase-downloads.html
- Eclipse Neon 2016 - Eclipse IDE for Java EE Developers - https://www.eclipse.org/neon/
- Jsoup library - https://jsoup.org/download

## Installation
- Create a new project
- Copy all files into the project
- Download Jsoup jar and place it in src->ucBusca folder

## Build and run
- compile all files: compile.bat
- run ```bash MulticastServer.java```, ```bash RequestHandler.java```, ```bash InternetAccess.java```, -cp ".;./jsoup-1.12.1.jar" file_name
- run all other files: java file_name localhost

## Architecture
![image](https://user-images.githubusercontent.com/41116942/111530353-15a76e00-875b-11eb-9487-1e5d81582503.png)

```bash
MulticastServer.java
```
