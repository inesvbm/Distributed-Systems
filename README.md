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
- run ```bash MulticastServer.java```, ```bash RequestHandler.java```, ```bash InternetAccess.java```, with ```-cp ".;./jsoup-1.12.1.jar" file_name```
- run all other files: ```java file_name localhost```

## Architecture
![image](https://user-images.githubusercontent.com/41116942/111530353-15a76e00-875b-11eb-9487-1e5d81582503.png)

### RMIClient
Allows users to access the Web search features. 

### RMI_interface_client_methods
Implements ```RMIClient``` interface - specifies RMIClient methods.

### RMI_interface_server_methods
Implements ```RMIServer``` interface - specifies RMIServer methods.

### RMIServer
- Communicates with ```RMIClient``` through remote methods (receives Client requests and sends ```MulticastServer``` answers).
- Communicates with ```MulticastServer``` through two multicast sockets: send and receive info in UDP datagrams. How to process a request:
    1) ```RMIClient``` sends request;
    2) ```RMIServer``` receives the client request and asks ```MulticastServer``` it's multicast Id, through a
       binded socket;
    3) After receive the multicast ID, the socket is closed. It is opened a new binded socket to send
       the client request to the ```MulticastServer```;
    4) Receives and handles the answer from ```MulticastServer``` to send it back to the client.
- A 30s Time Out is set, in case ```MulticastServer``` delays the answer.

### MulticastServer
Receives Id requests and client requests from ```RMIServer```. After receiving a request, it will send it to ```RequestHandler``` thread, which syncronizes request handling. This class stores the following info:
- Registered clients
- Logged clients
- Word inverted index
- Common searches
- Historic of searches of each client
This way, in case of failure, all client data is written in serializable object files: there is a thread that listens if the application fails and in this case, the data is immediately stored in files. When the application starts, the files are read and the data is stored in structures.

### RequestHandler
This class is a thread. For each request, a new thread from this class is created and will handle the specific request. If it is a multicast Id request, an Id will be generated and sent to the ```RMIServer```; if it is a client request, it will check if the multicast Id received with the client request is the same that was generated before. If yes, it will handle the request. If not, it will ignore it, since the request will be in charge of another MulticastServer.

### InternetAccess
Jsoup file to read and extract info from HTML files. It will send info to ```RequestHandler```, for URL indexing and/or searches.



## Features

### Registry
Clients are able to create an account, giving an username and password. After registry, the personal data will be stored.

### Login
Clients are able to login, giving username and password. If not registered, a message will be sent.

### Index new URL
Admins can manually insert an URL to be indexed by the automatic indexer (Web crawler). All words found in the text will be added to the word inverted index. 

### Search
Any user can make a search using a group of words. The web search engine will check the inverted index and display the list of Web pages that contain those words. Each result displays the page title, the complete URL and a short citation from that page. The total number of results must be displayed as well.

### Search results sorted by importance
A page is more important if it contains more links from other pages. So, the automatic indexer must keep for each URL, the list of other URLs which make the connection to it.

### Check list of pages with connection to a specific page
For logged users it is possible to know all known links that point to a specific page.

### Check historic searches
Logged clients are able to see their historic of searches.

### Admin page updated in real time
Admin page includes general system info: 10 more important pages, 10 more common searches, and active multicast servers.

### Give admin privileges to a user
Admins can attribute this privilege to other users.

### Real time admin privilege notifications
If a logged user gets admin privileges, he will receive a notification immediately.

### Offline admin privilege message delivery
If a user is not logged and receives admin privileges, he will see the message once he logs in.
