// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************


/**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  
  String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  //Exercice 3a)
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  //Exercice 2a)
  public void handleMessageFromClientUI(String message)
  {
    try
    {
        if(message.equals("#quit")) { 
        	quit();
        }
        else if(message.equals("#logoff")) {
        	closeConnection();
        }
        else if(message.regionMatches(0,"#sethost",0,7) && !(isConnected())){
        	setHost(message.substring(7));

        }
        else if(message.regionMatches(0,"#sethost",0,7) && (isConnected())){
        	System.out.println("Error, client must be disconnected");
        }
        else if(message.regionMatches(0,"#setport",0,7) && !(isConnected())) {
        	setPort(Integer.parseInt(message.substring(7)));

        }
        else if(message.regionMatches(0,"#setport",0,7) && isConnected()) {
        	System.out.println("Error, client must be disconnected");
        }
        else if(message.equals("#login") && !(isConnected())){
        	openConnection();
        }
        else if(message.equals("#login") && isConnected()) {
        	System.out.println("Error, client must be disconnected");
        }
        else if(message.equals("#gethost")) {
        	System.out.println(getHost());
        }
        else if(message.equals("#getport")) {
        	System.out.println(getPort());
        }
        else {
        	sendToServer(message);
        }
      

    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  //Exercice 1a)
  public void connectionClosed() {
	  System.out.println("System's connection has been closed");
  }
  //Exercice 1a)
  public void connectionException(Exception exception) {
	  System.out.println("Error! System not connected");
	  quit();
  }
  //Exercice 3b)
  public void connectionEstablished() {
	  try {
		sendToServer("#login<"+loginID+">");
	} catch (IOException e) {
	}
  }
}
//End of ChatClient class