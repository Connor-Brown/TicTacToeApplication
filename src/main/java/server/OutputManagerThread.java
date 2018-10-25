package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class OutputManagerThread extends Thread{
	
	private Socket clientSocket;
	private List<String> messageList = new ArrayList<>();

	public OutputManagerThread(Socket client) {
		clientSocket = client;
	}
	
	@Override
	public void run() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			while(messageList.isEmpty()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			String s = messageList.remove(0);
			out.println(s);
		}
	}
	
	public synchronized List<String> getMessageList() {
		return messageList;
	}

}
