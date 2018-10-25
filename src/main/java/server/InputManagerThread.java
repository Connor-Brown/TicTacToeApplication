package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.Board;
import main.MonteCarloAI;
import main.Move;
import main.Node;

public class InputManagerThread extends Thread {

	private Socket clientSocket;
	private List<String> outputList;

	public InputManagerThread(Socket client, List<String> list) {
		clientSocket = client;
		if(list != null)
			outputList = list;
		else
			outputList = new ArrayList<>();
	}

	@Override
	public void run() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String inputLine;
		List<Wrapper> wrapperList = new ArrayList<>();

		try {
			while ((inputLine = in.readLine()) != null) {
//				System.out.println(inputLine);
				// check if it is from a new session, if so create a new RunGameSession
				// else distribute the message to the appropriate thread
				String identifier = getIdentifier(inputLine);
				inputLine = inputLine.substring(identifier.length() + 1, inputLine.length());
				Wrapper w = searchForWrapper(wrapperList, identifier);
				if (w == null) {
					w = createNewWrapper(wrapperList, identifier);
				}
				// do what the message tells you to do
				if(isSingleplayer(identifier)) {
					if (!inputLine.equals("")) {
						if (w.getBoard().isGameOver()) {
							outputList.add(w.getIdentifier() + " GAMEOVER " + w.getBoard().getWinner());
							wrapperList.remove(w);
						} else {
							Move move = new Move(inputLine);
							w.getBoard().doMove(move.getRow(), move.getCol());
							w.getBoard().nextTurn();
							if (w.getBoard().isGameOver()) {
								outputList.add(w.getIdentifier() + " GAMEOVER " + w.getBoard().getWinner());
								wrapperList.remove(w);
							} else {
								w.getAi().doTurn(w.getBoard());
								w.getBoard().nextTurn();
								if (w.getBoard().isGameOver()) {
									outputList.add(w.getIdentifier() + " GAMEOVER " + w.getBoard().getWinner());
									wrapperList.remove(w);
								} else {
									outputList.add(w.getIdentifier() + " " + w.getBoard());
								}
							}
						}
					} else {
						outputList.add(w.getIdentifier() + " " + w.getBoard());
					}
				} else {
					if(!inputLine.equals("")) {
						if(w.getBoard().isGameOver()) {
							outputList.add(w.getIdentifier() + " GAMEOVER " + w.getBoard().getWinner());
							wrapperList.remove(w);
							Wrapper other = getOtherIdentifierInServer(w.getIdentifier(), wrapperList);
							wrapperList.remove(other);
							outputList.add(other.getIdentifier() + " GAMEOVER " + other.getBoard().getWinner());
						} else {
							Move move = new Move(inputLine);
							w.getBoard().doMove(move.getRow(), move.getCol());
							w.getBoard().nextTurn();
							if(w.getBoard().isGameOver()) {
								outputList.add(w.getIdentifier() + " GAMEOVER " + w.getBoard().getWinner());
								wrapperList.remove(w);
								Wrapper other = getOtherIdentifierInServer(w.getIdentifier(), wrapperList);
								wrapperList.remove(other);
								outputList.add(other.getIdentifier() + " GAMEOVER " + other.getBoard().getWinner());
							} else {
								outputList.add(w.getIdentifier() + " " + w.getBoard() + " NO");
								Wrapper other = getOtherIdentifierInServer(w.getIdentifier(), wrapperList);
								other.setBoard(w.getBoard());
								if(other != null)
									outputList.add(other.getIdentifier() + " " + other.getBoard() + " OK");
							}
						}
					} else {
						//check if server has two players already. if so return CONNECT NO, else CONNECT OK
						int count = getNumPlayersInServer(identifier, wrapperList);
						if(count > 2) {
							wrapperList.remove(w);
							outputList.add(w.getIdentifier() + " CONNECT NO");
						} else {
							if(count == 1)
								outputList.add(w.getIdentifier() + " CONNECT WA "+w.getBoard() + " NO");
							else {
								outputList.add(w.getIdentifier() + " CONNECT OK "+w.getBoard() + " NO");
								Wrapper other = getOtherIdentifierInServer(w.getIdentifier(), wrapperList);
								outputList.add(other.getIdentifier() + " " + other.getBoard() + " OK");
							}
						}
					}
				}
				// print desired message to output list with identifier
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Wrapper getOtherIdentifierInServer(String identifier, List<Wrapper> wrapperList) {
		Scanner scan = new Scanner(identifier);
		scan.useDelimiter("-");
		String id = scan.next();
		scan.close();
		for(Wrapper w : wrapperList) {
			scan = new Scanner(w.getIdentifier());
			scan.useDelimiter("-");
			String checker = scan.next();
			scan.close();
			if(!identifier.equals(w.getIdentifier()) && id.equals(checker))
				return w;
		}
		return null;
	}

	private int getNumPlayersInServer(String identifier, List<Wrapper> wrapperList) {
		int sum = 0;
		Scanner scan = new Scanner(identifier);
		scan.useDelimiter("-");
		String first = scan.next();
		scan.close();
		for(Wrapper w : wrapperList) {
			scan = new Scanner(w.getIdentifier());
			scan.useDelimiter("-");
			String f = scan.next();
			scan.close();
			if(f.equals(first))
				sum++;
		}
		return sum;
	}

	private boolean isSingleplayer(String identifier) {
		if(identifier.contains("-"))
			return false;
		return true;
	}

	private Wrapper createNewWrapper(List<Wrapper> wrapperList, String identifier) {
		Node r = new Node();
		Board b = new Board();
		r.setBoard(b);
		MonteCarloAI m = new MonteCarloAI('X', r);
		wrapperList.add(new Wrapper(b, r, m, identifier));
		return wrapperList.get(wrapperList.size() - 1);
	}

	private String getIdentifier(String inputLine) {
		Scanner scan = new Scanner(inputLine);
		String identifier = scan.next();
		scan.close();
		return identifier;
	}

	private Wrapper searchForWrapper(List<Wrapper> wrapperList, String identifier) {
		for (Wrapper w : wrapperList) {
			if (w.getIdentifier().equals(identifier))
				return w;
		}
		return null;
	}

}
