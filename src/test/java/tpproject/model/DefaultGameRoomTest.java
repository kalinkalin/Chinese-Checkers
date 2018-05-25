package tpproject.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class DefaultGameRoomTest
{
	final static int portNumber = 9090;
	private static BufferedReader input1;
	private static PrintWriter output1;
	private static BufferedReader input2;
	private static PrintWriter output2;
	private static Socket socket1;
	private static Socket socket2;
	
	private static String returnCommands[];
	
	@BeforeClass
	public static void initialize() throws UnknownHostException, IOException, InterruptedException {
		
		returnCommands = new String[11];
		socket1 = new Socket("localhost", portNumber);
		input1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
		output1 = new PrintWriter(socket1.getOutputStream(),true);
		socket2 = new Socket("localhost", portNumber);
		input2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
		output2 = new PrintWriter(socket2.getOutputStream(),true);
		
		output1.println("JOIN");
		returnCommands[0] = input1.readLine();			//SUCCESS
		output2.println("JOIN");
		returnCommands[1] = input2.readLine();			//SUCCESS
		output1.println("START");
		returnCommands[2] = input1.readLine();         //GAME_STARTED
		returnCommands[3] = input1.readLine();         //RED
		returnCommands[4] = input1.readLine();         //PLAYERS_NUMBER
		returnCommands[5] = input2.readLine();		   //GAME_STARTED
		returnCommands[6] = input2.readLine();         //YELLOW
		input2.readLine();                             //PLAYERS_NUMBER
		socket1.close();
		socket2.close();
	}
	

	@Test
	public void shouldJoinNewGame(){
		assertEquals("JOINED", returnCommands[0]);
	}
	
	@Test
	public void secondPlayerShouldJoinNewGame(){
		assertEquals("JOINED", returnCommands[1]);
	}
	
	@Test 
	public void shouldSetUpGameforTwoPlayers(){
		assertEquals("GAME_STARTED", returnCommands[2]);
		assertEquals("GAME_STARTED", returnCommands[5]);
	}
	
	@Test
	public void shouldGiveBackTheColorValue(){
		assertEquals("YELLOW", returnCommands[3]);
		assertEquals("RED", returnCommands[6]);
		assertEquals("2", returnCommands[4]);
	}
	
}
