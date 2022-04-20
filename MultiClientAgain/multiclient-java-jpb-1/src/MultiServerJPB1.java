
import java.io.*;
import java.net.*;
import java.util.*;







public class MultiServerJPB1 {
    
    private static final int SERVER_PORT = 8765;
    private static String username = "root";

    
    public static void main(String[] args) {
        //createCommunicationLoop();
        createMultithreadCommunicationLoop();
    }//end main

    static Vector<ClientHandler> activeClients = new Vector<>();

    public static void createMultithreadCommunicationLoop() {
        int clientNumber = 0;



        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            Socket socket;
            System.out.println("Server started on " + new Date() + ".");
            //listen for new connection request
            while(true) {


                socket = serverSocket.accept();

                DataInputStream dainst = new DataInputStream(socket.getInputStream());
                DataOutputStream daoust = new DataOutputStream(socket.getOutputStream());

                ClientHandler newClient = new ClientHandler(socket, "client " + clientNumber, dainst, daoust);



                //Find client's host name 
                //and IP address
                InetAddress inetAddress = socket.getInetAddress();
                System.out.println("Connection from client " + 
                        clientNumber);
                System.out.println("\tHost name: " + 
                        inetAddress.getHostName());
                System.out.println("\tHost IP address: "+
                        inetAddress.getHostAddress());
                
                //create and start new thread for the connection
                Thread clientThread = new Thread(newClient);
                activeClients.add(newClient);
                clientThread.start();
                clientNumber++;  //increment client number
            }//end while           
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
    }//end createMultithreadCommunicationLoop
    
    public static void createCommunicationLoop() {
        try {
            double num1 = 0.00;
            double num2 = 0.00;
            double answer = 0.00;
            double answer2 = 0.00; //these set up the variables for later

            String newListName = username +"_solutions.txt"; //creates the file of the username with the solutions.
            File newList = new File(newListName);
            String returningAnswers = null;
            String numbersOnly = null;
            Scanner giveList = new Scanner(newList);
            FileOutputStream theList = new FileOutputStream(newList);
            DataOutputStream listWrite = new DataOutputStream(new BufferedOutputStream(theList)); //sorts through the text lists
            String givingList = "";



            //create server socket
            ServerSocket serverSocket =
                    new ServerSocket(SERVER_PORT);

            System.out.println("Server started at " +
                    new Date() + "\n");
            //listen for a connection
            //using a regular *client* socket
            Socket socket = serverSocket.accept();

            //now, prepare to send and receive data
            //on output streams
            DataInputStream inputFromClient =
                    new DataInputStream(socket.getInputStream());

            DataOutputStream outputToClient =
                    new DataOutputStream(socket.getOutputStream());

            //server loop listening for the client 
            //and responding
            while(true) {
                String strReceived = inputFromClient.readUTF();

                if(strReceived.equalsIgnoreCase("hello")) {
                    System.out.println("Sending hello to client");
                    outputToClient.writeUTF("hello client!");
                }else if (strReceived.contains("SOLVE") || (strReceived.contains("solve"))) {


                    numbersOnly = strReceived.replaceAll("[^0-9]", "");


                    if (strReceived.contains("-r"))
                    {
                        if(numbersOnly.length() == 2) //checks if there's two numbers
                        {
                            num1 = Character.getNumericValue(numbersOnly.charAt(0));
                            num2 = Character.getNumericValue(numbersOnly.charAt(1));

                            answer = (num1 * 2) + (num2 * 2);
                            answer2 = num1 * num2;
                            returningAnswers = "Sides " + num1 + " " + num2 + ": Rectangle’s perimeter and area is: " + answer + ", " + answer2;
                            listWrite.writeUTF(returningAnswers + "\n");
                            outputToClient.writeUTF(returningAnswers);
                        }
                        else if(numbersOnly.length() == 1) //checks if there's one number
                        {
                            num1 = Character.getNumericValue(numbersOnly.charAt(0));
                            num2 = Character.getNumericValue(numbersOnly.charAt(0));
                            answer = (num1 * 2) + (num2 * 2);
                            answer2 = num1 * num2;
                            returningAnswers = "Sides " + num1 + " " + num2 + ": Square’s perimeter and area is: " + answer + ", " + answer2;
                            listWrite.writeUTF(returningAnswers + "\n");
                            outputToClient.writeUTF(returningAnswers);
                        }
                        else
                        {
                            System.out.println("Wrong numbers");
                            returningAnswers = "Error: No sides found.";
                            listWrite.writeUTF(returningAnswers + "\n");
                            outputToClient.writeUTF(returningAnswers);
                        }
                    } else if (strReceived.contains("-c")) {
                        double pi = 3.14;

                        if(numbersOnly.length() == 1) //checks if there's one number
                        {
                            num1 = Character.getNumericValue(numbersOnly.charAt(0));
                            answer = 2 * pi * num1;
                            returningAnswers = "Circumference of circle is: " + answer;
                            listWrite.writeUTF(returningAnswers + "\n");
                            outputToClient.writeUTF(returningAnswers);

                        }
                        else
                        {
                            System.out.println("Wrong numbers");

                            returningAnswers = "Error: No radius found or other error.";
                            listWrite.writeUTF(returningAnswers + "\n");
                            outputToClient.writeUTF(returningAnswers);
                        }

                    }
                    else
                    {
                        System.out.println("No numbers");
                        outputToClient.writeUTF("Error: Please append -r or -c to the statement you're attempting.");
                    }

                } else if (strReceived.contains("LIST") || (strReceived.contains("list"))) {
                    {
                        if ((strReceived.contains("-all")) && (username.equalsIgnoreCase("root"))) { //root access works but program does not know how to make other lists or sort through them.
//                            String appended = "_solutions.txt";
//
//                            File f = new File("src");
//                            File[] textFiles = f.listFiles(textFilter);
//                            for(int j = 0; j < textFiles.length(); j++)
//                            {
//
//                            }
                            outputToClient.writeUTF("You have root access, but this file does not have the ability to have other users or access their files. Please try again without the -all.");

                        } else if ((strReceived.contains("-all")) && (!username.equalsIgnoreCase("root"))) {
                            outputToClient.writeUTF("Error: you are not the root user");
                        } else {
                            givingList += username + "\n";

                            while(giveList.hasNextLine())
                            {
                                givingList += giveList.nextLine() + "\n";
                            }
                            outputToClient.writeUTF(givingList); //writing into root_solutions.txt will cause this line to work as intended.
                        }
                    }
                }




                
                else if(strReceived.equalsIgnoreCase("quit")) {
                    System.out.println("Shutting down server...");
                    outputToClient.writeUTF("Shutting down server...");
                    serverSocket.close();
                    socket.close();
                    break;  //get out of loop
                }
                else {
                    System.out.println("Unknown command received: "
                        + strReceived);
                    outputToClient.writeUTF("300 invalid command");

                }
            }//end server loop
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch
    }//end createCommunicationLoop
}
