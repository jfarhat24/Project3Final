
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
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
                        String newName = strReceived;

                        System.out.println("Say who you're sending your message to." +
                                clientNumber);
                        String newText = strReceived;
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
