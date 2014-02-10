package testapp2_server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import org.quickconnectfamily.json.*;


public class Server_main {

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {

		try {
			//a socket opened on the specified port
			@SuppressWarnings("resource")
			ServerSocket aListeningSocket = new ServerSocket(6000);
			while(true){
				//wait for a connection
				System.out.println("Waiting for client connection request.");
				Socket clientSocket = aListeningSocket.accept();
				//setup the JSON streams for later use.
				JSONInputStream inFromClient = new JSONInputStream(clientSocket.getInputStream());
				JSONOutputStream outToClient = new JSONOutputStream(clientSocket.getOutputStream());
				//read until the client closes 
				//the connection.
				while(true){
					System.out.println("Waiting for a message from the server.");
					HashMap aMap = (HashMap)inFromClient.readObject();
					System.out.println("Just got:"+aMap+" from client");
					CommunicationBean aResponse = new CommunicationBean("Done",(ArrayList)aMap.get("data"));

					outToClient.writeObject(aResponse);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}