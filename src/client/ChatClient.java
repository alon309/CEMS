// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import server.EchoServer;
import client.*;
import gui.QuestionBankController;
import gui.ServerPortFrameController;
import ClientServerComm.ChatIF;
import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;

import Config.ConnectedClient;
import Config.Question;

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
	static ChatClient Instance_host;
	
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  public static Question  q1 = new Question(null,null,null,null,null,null);
  public static boolean awaitResponse = false;

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
	 
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    Instance_host = this;
    openConnection();
    getInetAddress();
	ServerPortFrameController.addConnectedClient(new ConnectedClient(InetAddress.getLocalHost().getHostAddress(), InetAddress.getLocalHost().getHostName()));
  }

  //Instance methods ************************************************
  public static ChatClient getInstance_host() {
      return Instance_host;
  }
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
	  System.out.println("--> handleMessageFromServer");
	  awaitResponse = false;
	  
	  if(msg instanceof ArrayList) { // get the arraylist from server and set in the table
		  ArrayList<Question> questions = (ArrayList<Question>)msg;
		  QuestionBankController.getInstance().loadArrayQuestionsToTable(questions);
		  System.out.println("The questions succesfully loaded from the DB to the table.");
	  }
	  else {
		  System.out.println(msg);
	  }
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  
  public void handleMessageFromClientUI(String message)  
  {
    try
    {
    	openConnection();//in order to send more than one message
       	awaitResponse = true;
    	sendToServer(message);
		// wait for response
		while (awaitResponse) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    catch(IOException e)
    {
    	e.printStackTrace();
      clientUI.display("Could not send message to server: Terminating client."+ e);
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
}
//End of ChatClient class