import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
/*
1. The server keeps track of all users – track who, when, how long the user attached to the
server.
2. The server should wait for the client’s request. Upon attachment, the server should log
the details about the client.
3. The server can have connections simultaneously with multiple clients.
4. The server should be able to accept the string request for basic math calculations and
show who has sent what request.
5. The server should respond to clients in order of requests it gets from different clients.
6. The server should close connection with client upon request from the client and log it.
*/

class Server {

  public static void main(String argv[]) throws Exception
    {
      ServerSocket welcomeSocket = new ServerSocket(6789);
      System.out.println("SERVER UP AND RUNNING!");

      while(true) {
        Socket connectionSocket = welcomeSocket.accept();
        System.out.println("New client connected: " + connectionSocket.getInetAddress()); 
            
        ClientHandler handler = new ClientHandler(connectionSocket); 
        Thread thread = new Thread(handler); 
        thread.start(); 
        }
    }
}

class ClientHandler implements Runnable {
  private Socket clientSocket; 
  private LocalDateTime connectTime; 
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public ClientHandler(Socket socket) {
    this.clientSocket = socket; 
    this.connectTime = LocalDateTime.now();
  }

  @Override
  public void run() {
    try {
      BufferedReader inFromClient = new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));
      DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());

      String request; 
      while ((request = inFromClient.readLine()) != null){
        String[] split = request.split("\\|");  

        // to handle different request types
        switch (split[0]) {
            case "JOIN":
              // print client  when connected
              String message = "Client " + split[1] + " successfully connected at " + connectTime.format(formatter) + "\n"; 
              System.out.print(message);
              outToClient.writeBytes(message);  
              break;
            case "CALC": 
              int result = 0;
              int operand1 = Integer.parseInt(split[3]);
              int operand2 = Integer.parseInt(split[4]); 
              String resultToClient = ""; 
              switch(split[2]) {
                case "+": 
                  result = operand1 + operand2; 
                  resultToClient =  operand1 + " + " + operand2 + " = " + result; 
                  System.out.println("Client " + split[1] + " performed: " + operand1 + " + " + operand2 + " = " + result); 
                  break; 
                case "-": 
                  result = operand1 - operand2; 
                  resultToClient =  operand1 + " - " + operand2 + " = " + result; 
                  System.out.println("Client " + split[1] + " performed: " + operand1 + " - " + operand2 + " = " + result); 
                  break; 
                case "*": 
                  result = operand1 * operand2; 
                  resultToClient =  operand1 + " * " + operand2 + " = " + result; 
                  System.out.println("Client " + split[1] + " performed: " + operand1 + " * " + operand2 + " = " + result); 
                  break; 
                case "/": 
                  if (operand2 != 0) {
                    result = operand1 / operand2; 
                  }
                  else {
                    result = -1; 
                    System.out.println("Division by 0 - error!"); 
                  }
                  resultToClient =  operand1 + " / " + operand2 + " = " + result; 
                  System.out.println("Client " + split[1] + " performed: " + operand1 + " / " + operand2 + " = " + result); 
                  break; 
                case "%": 
                  result = operand1 % operand2; 
                  resultToClient =  operand1 + " % " + operand2 + " = " + result; 
                  System.out.println("Client " + split[1] + " performed: " + operand1 + " % " + operand2 + " = " + result); 
                  break;
                default: 
                  break;  
              }
              outToClient.writeBytes(resultToClient + "\n");
              break; 
            case "EXIT":
              LocalDateTime disconnectTime = LocalDateTime.now(); 
              Duration duration = Duration.between(connectTime, disconnectTime);
              long seconds = duration.getSeconds();
              System.out.println("Client " + split[1] + " connection close. Total session length: " + seconds + " seconds."); 
              outToClient.writeBytes("Client " + split[1] + " connection closed"); 
              clientSocket.close();
              break;  
            default:
                System.out.println("Server error");
        }
      }
    } catch (IOException e) { // catches errors during client communication
      System.err.println("Client exception: " + e.getMessage()); 
    }
  }
}

