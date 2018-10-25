package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RunGameSession extends Thread{

	private Socket clientSocket;
	private List<String> messageList = new ArrayList<>();
	
	public RunGameSession(Socket client) {
		clientSocket = client;
	}
	
	@Override
	public void run() {
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String inputLine;

		Board b = new Board();
		Node root = new Node();
		root.setBoard(b);
		MonteCarloAI m = new MonteCarloAI('X', root);
//		System.out.println(b);
		out.println(b);
		try {
			while((inputLine = in.readLine()) != null) {
				if(b.isGameOver()) {
					out.println("GAMEOVER "+b.getWinner());
					break;
				}
				//get move
				Move move = new Move(inputLine);
				//do move
				b.doMove(move.getRow(), move.getCol());
				b.nextTurn();
				//do ai turn
				if(b.isGameOver()) {
					out.println("GAMEOVER "+b.getWinner());
					break;
				}
				m.doTurn(b);
				b.nextTurn();
				if(b.isGameOver()) {
					out.println("GAMEOVER "+b.getWinner());
					break;
				}
				//return board to client for them to do move
				out.println(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getMessageList() {
		return messageList;
	}

}
