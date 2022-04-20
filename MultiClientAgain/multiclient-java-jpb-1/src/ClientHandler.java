
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class loginsList {
    public String Username;
    public String PW;
}


//Runnable class allows us to create a task
//to be run on a thread
public class ClientHandler extends Thread implements Runnable {
    private Socket socket;  //connected socket
    private ServerSocket serverSocket;  //server's socket
    private int clientNumber; //counts up as clients join
    static Vector<ClientHandler> handlers = new Vector<ClientHandler>(20);
    private String IDName;
    private String IPAddress;
    DataInputStream dis;
    DataOutputStream dos;
    private boolean isRoot = false;
    private boolean isLogin = false;
    private String str;


    loginsList Ll[] = new loginsList[5];
    File messagefile = new File("/ThisFolder/");

    private BufferedReader bR = new BufferedReader(new FileReader(messagefile));
    int numofMessages = 0;
    Vector messages = new Vector();
    int messageTracker = 0;

    public ClientHandler(Socket socket, String s, DataInputStream dainst, DataOutputStream daoust) throws IOException {
    this.socket = socket;
    this.dis = dainst;
    this.dos = daoust;
    this.isLogin = true;
    }

    //create an instance
    public ClientHandler(int clientNumber, Socket socket, ServerSocket serverSocket) throws FileNotFoundException {
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.clientNumber = clientNumber;
    }//end ctor


    //run() method is required by all
    //Runnable implementers
    @Override
    public void run() {
        //run the thread in here


        try {



            while ((str = bR.readLine()) != null) {
                messages.add(str);
                numofMessages++;
            }
        }catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

            for(int i = 0; i < 5; i++)
            {
                Ll[i] = new loginsList();
            }

            //input login text and passwords here

            boolean firstLine = false;
            boolean firstRun = false;

            String line;

            synchronized(handlers)
            {
                handlers.addElement(this);
            }

            try
            {
                double num1 = 0.00;
                double num2 = 0.00;
                double answer = 0.00;
                double answer2 = 0.00; //these set up the variables for later

                String newListName = IDName +"_solutions.txt"; //creates the file of the IDName with the solutions.
                File newList = new File(newListName);
                String returningAnswers = null;
                String numbersOnly = null;
                Scanner giveList = new Scanner(newList);
                FileOutputStream theList = new FileOutputStream(newList);
                DataOutputStream listWrite = new DataOutputStream(new BufferedOutputStream(theList)); //sorts through the text lists
                String givingList = "";

            DataInputStream inputFromClient =
                    new DataInputStream(socket.getInputStream());
            
            DataOutputStream outputToClient = 
                    new DataOutputStream(socket.getOutputStream());
            
            
            //continuously serve the client
            while(true) {                
                String strReceived = inputFromClient.readUTF();
                System.out.println("\n\t[[Command " + strReceived +
                        " received from client " + clientNumber +"]]");
                
                if(strReceived.equalsIgnoreCase("hello")) {
                    System.out.println("Sending hello to client " +
                            clientNumber);
                    outputToClient.writeUTF("hello client " +
                            clientNumber + "!");
                }
                else if(strReceived.equalsIgnoreCase("message"))
                {
                    if(!isLogin)
                    {
                        System.out.println("Not logged in, please log in!");
                    }
                    else
                    {
                        System.out.println("Say who you're sending your message to." +
                                clientNumber);
                        String newName = strReceived; //takes in the name of the person's intended target

                        System.out.println("Say who you're sending your message to." +
                                clientNumber);
                        String newText = strReceived; //takes in the message
                        outputToClient.writeUTF(newName + newText);
                    }
                }
                else if(strReceived.equalsIgnoreCase("logout")) {

                    if(!isLogin)
                    {
                        System.out.println("Not logged in, please log in!");
                    }
                    else
                    {
                        this.isLogin = false;
                        this.socket.close();
                        break;
                    }




                }
                else if(strReceived.equalsIgnoreCase("quit")) {

                    if(!isRoot)
                    {
                        System.out.println("Not logged in as Root!"); //this would prevent a full shutdown from happening without root
                    }
                    else
                    {
                        System.out.println("Shutting down server...");
                        outputToClient.writeUTF("Shutting down server...");
                        serverSocket.close(); //closes the server
                        socket.close();
                        break;  //get out of loop
                    }


                }
                else if (strReceived.contains("SOLVE") || (strReceived.contains("solve"))) {


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
                        if ((strReceived.contains("-all")) && (IDName.equalsIgnoreCase("root"))) { //root access works but program does not know how to make other lists or sort through them.
//                            String appended = "_solutions.txt";
//
//                            File f = new File("src");
//                            File[] textFiles = f.listFiles(textFilter);
//                            for(int j = 0; j < textFiles.length(); j++)
//                            {
//
//                            }
                            outputToClient.writeUTF("You have root access, but this file does not have the ability to have other users or access their files. Please try again without the -all.");

                        } else if ((strReceived.contains("-all")) && (!IDName.equalsIgnoreCase("root"))) {
                            outputToClient.writeUTF("Error: you are not the root user");
                        } else {
                            givingList += IDName + "\n";

                            while(giveList.hasNextLine())
                            {
                                givingList += giveList.nextLine() + "\n";
                            }
                            outputToClient.writeUTF(givingList); //writing into root_solutions.txt will cause this line to work as intended.
                        }
                    }
                }
                else {
                    System.out.println("Unknown command received: " 
                        + strReceived);
                    outputToClient.writeUTF("Unknown command.  "
                            + "Please try again.");
                    
                }
            }//end while
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch

    }//end run
    
}//end ClientHandler
