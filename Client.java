import java.io.*;
import java.net.*;


class Client {

    public static void main(String argv[]) throws Exception
    {
        String name;   
        System.out.println("CLIENT IS RUNNING!" );
        
        Socket clientSocket = new Socket("127.0.0.1", 6789);
         //read input from the user
        BufferedReader inFromUser =
          new BufferedReader(new InputStreamReader(System.in));
        //get response from server
        BufferedReader inFromServer =
                new BufferedReader(new
                InputStreamReader(clientSocket.getInputStream()));
        //Send request to server
        DataOutputStream outToServer =
          new DataOutputStream(clientSocket.getOutputStream());
        
        boolean exit = false;

        while (true && !exit) {
          // track who, when, how long the user attached to the server
          // get name of client and send join request to server
          System.out.print("Enter your name: "); 
          name = inFromUser.readLine(); 
          outToServer.writeBytes("JOIN|" + name + "\n"); 
          String joinResponse = inFromServer.readLine();
          System.out.println("FROM SERVER: " + joinResponse); 
        //prompt user to input math expression
          while(!exit) {
              System.out.print("Enter math expression (i.e. 1 + 2 - 3) or type 'exit' to quit: ");
              String operatorFull = inFromUser.readLine(); 

              if (operatorFull.equalsIgnoreCase("exit")) {
                outToServer.writeBytes("EXIT|" + name + "\n");
                String response = inFromServer.readLine();
                System.out.println("FROM SERVER: " + response); 
                exit = true; 
                clientSocket.close();
              }
              else {
                outToServer.writeBytes("CALC|" + name + "|" + operatorFull + "\n");

    
                // wait for response from server with the result
                String result = inFromServer.readLine();
                System.out.println("FROM SERVER: " + result);
              }
          }         
        }
      }
}
