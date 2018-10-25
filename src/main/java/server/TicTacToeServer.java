package server;

import java.io.*;
import java.net.*;

import main.RunGameSession;

public class TicTacToeServer {
	
	static int port = 4444;

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Couldn't listen on port: "+port);
			System.exit(1);
		}
		
		Socket clientSocket = null;
		try {
			clientSocket = serverSocket.accept();
			OutputManagerThread o = new OutputManagerThread(clientSocket);
			o.start();
			new InputManagerThread(clientSocket, o.getMessageList()).start();;
//			new InputManagerThread(clientSocket, null).start();
//			while(((clientSocket = serverSocket.accept()) != null)) {
//				System.out.println("new Connection");
//				new RunGameSession(clientSocket).start();
//			}
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}
	}

}
