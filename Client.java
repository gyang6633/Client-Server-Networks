import java.io.*;
import java.net.*;
class Client {

    public static void main(String argv[]) throws Exception
    {
        String name;   
        int operand1; 
        int operand2;
        char operator;
        String result;  
        System.out.println("CLIENT IS RUNNING!" );

        Socket clientSocket = new Socket("127.0.0.1", 6789);

        BufferedReader inFromUser =
          new BufferedReader(new InputStreamReader(System.in));

        BufferedReader inFromServer =
                new BufferedReader(new
                InputStreamReader(clientSocket.getInputStream()));

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

          while(!exit) {
              System.out.print("Enter operator (+, -, *, /, %) or type 'exit' to quit: ");
              String operatorFull = inFromUser.readLine(); 

              if (operatorFull.equalsIgnoreCase("exit")) {
                outToServer.writeBytes("EXIT|" + name + "\n");
                String response = inFromServer.readLine();
                System.out.println("FROM SERVER: " + response); 
                exit = true; 
                clientSocket.close();
              }
              else {
                operator = operatorFull.charAt(0); 
                System.out.print("Enter the first operand: "); 
                operand1 = Integer.parseInt(inFromUser.readLine());
                System.out.print("Enter the second operand: "); 
                operand2 = Integer.parseInt(inFromUser.readLine());
                outToServer.writeBytes("CALC|" + name + "|" + operator + "|" + operand1 + "|" + operand2 + "\n");
    
                // wait for response from server with the result
                result = inFromServer.readLine();
                System.out.println("FROM SERVER: " + result);
              }
          }         
        }
      }
}
