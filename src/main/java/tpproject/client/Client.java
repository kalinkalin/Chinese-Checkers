package tpproject.client;

import java.io.*;
import java.net.Socket;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tpproject.model.*;
import javafx.scene.control.*; 
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.*;
import javafx.scene.shape.StrokeType;

public class Client extends Application {
	
	static final int MARBLE_DIAMETER_SIZE = 36;
	static final int MARBLE_RADIUS_SIZE = 18;
	
	private String playerColor;
	private boolean isJoined;
	private boolean isWon;
	
	private Marble oldField;
	private Marble newField;
	
	private BufferedReader clientInput;
	private PrintWriter clientOutput;
	private Socket clientSocket;
	private String command;
	
	private Scene primaryScene;
	private MenuBar menu;
	private Menu game;
	private Menu action;
	private MenuItem pass;
	private MenuItem join;
	private MenuItem start;
	private MenuItem quit;
	private TextArea text;
	private Board board;
	private List<Field> fields;
	private Group marblesGroup;
	
	public static void main(String args[]) throws IOException {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		BorderPane borderPane = new BorderPane();
		primaryStage.setTitle("Chinese Checkers");
		
		marblesGroup = new Group();
		text = new TextArea();
		text.setPrefSize(200, 100);
		text.setMaxWidth(400);
		text.setMaxHeight(100);
		text.setEditable(false);
		primaryScene = new Scene(borderPane,900,600);
		menu = this.menuBarBuilder();
		
		borderPane.setCenter(marblesGroup);
		borderPane.setTop(menu);
		borderPane.setBottom(text);
		
		primaryStage.setScene(primaryScene);
		primaryStage.setOnCloseRequest(e ->
					{
						if(clientOutput != null) {
							clientOutput.println("QUIT");
							try {
								clientSocket.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							Platform.exit();
						}
					});        
		primaryStage.show();
		this.connectToServer();
		
		new Thread() {
			@Override
			public void run() {
				try {
					while(clientInput != null) {
						
						try {
							command = clientInput.readLine();
						}
						catch(IOException ex) {
							clientSocket.close();
							return;
						}
						if(command ==  null)
							return;
						
						if(command.equals("JOINED")) {
							printUserMessage("You have joined the game successfully.\n");
							isJoined = true;
						}
						
						else if(command.equals("FAIL") && !isJoined) {
							printUserMessage("Failed during joining the game.\n");
						}
						
						else if(command.equals("GAME_STARTED")) {
							
							String playerNumber;
							playerColor = clientInput.readLine();
							playerNumber = clientInput.readLine();
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									setUpGame(playerColor,playerNumber, marblesGroup);
								}
							});	
							printUserMessage("The game has started, your color is " + playerColor + ".\n");
						}
						
						else if(command.equals("WRONG_MOVE")) {
							printUserMessage("Wrong move !\n");
						}
						
						else if(command.equals("FINISH")) {
							
							printUserMessage("Wait for your turn.\n");
							
							if(oldField != null && newField != null) {
								Color temp = (Color) oldField.getFill();
								oldField.setFill(Color.valueOf("#e4e4e4"));
								newField.setColor(oldField.getColor());
								oldField.setColor("NONE");
								oldField.setStroke(Color.BLACK);
								oldField.setStrokeWidth(1);
								oldField.setStrokeType(StrokeType.INSIDE);
								newField.setStroke(Color.GOLD);
								newField.setStrokeType(StrokeType.INSIDE);
								newField.setStrokeWidth(3);
								newField.setFill(temp);
								oldField = newField;
								newField = null;
							}
						}
						
						else if( command.equals("MOVE_AGAIN")) {
							
							printUserMessage("You can move again.\n");
							Color temp = (Color) oldField.getFill();
							oldField.setFill(Color.valueOf("#e4e4e4"));
							newField.setColor(oldField.getColor());
							oldField.setColor("NONE");
							newField.setFill(temp);
							oldField.setStroke(Color.BLACK);
							oldField.setStrokeWidth(1);
							oldField.setStrokeType(StrokeType.INSIDE);
							newField.setStroke(Color.GOLD);
							newField.setStrokeType(StrokeType.INSIDE);
							newField.setStrokeWidth(3);
							oldField = newField;
							newField = null;
						}
						
						else if(command.equals("MOVE")) {
							printUserMessage("It is your turn to move.\n");
						}
						
						else if(command.startsWith("MOVED ")) {
							String movingCommand = command;
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									moveOpponentsMarble(movingCommand);
								}
							});	
						}
						
						else if(command.equals("PASSED")) {
							printUserMessage("Wait for your turn.\n");
						}
						
						else if((command.startsWith("WIN"))) {
							
							String[] splited = command.split("\\s+");
							
							if(splited[1].equals("1")) {
								text.appendText("Congratulations! you are WINNER\n");
							}
							else {
								text.appendText("Congratulations, you have finished, your place : " + splited[1]);
							}

							isWon = true;
						}
					}
				} catch (IOException e) {
					try {
						clientSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	private void connectToServer() {
		try {
			clientSocket = new Socket("localhost",9090);
		} catch (IOException e) {
			Platform.exit();
			return;
		}
		try {
			clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			text.appendText("Cannot connect to server");
		}
		try {
			clientOutput = new PrintWriter(clientSocket.getOutputStream(),true);
		} catch (IOException e) {
			text.appendText("Cannot connect to sever");
		}
	}
	
	private MenuBar menuBarBuilder() {
		MenuBar menuBar = new MenuBar();
		game = new Menu("GAME");
		action = new Menu("ACTION");
		join = new MenuItem("JOIN");
		
		join.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					clientOutput.println("JOIN");
			}
		});
				
		start = new MenuItem("START");
		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					clientOutput.println("START");
			}
		});
		
		quit = new MenuItem("QUIT");
		quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					clientOutput.println("QUIT");
					Platform.exit();
			}
		});
		
		pass = new MenuItem("PASS");
		pass.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
					clientOutput.println("PASS");
			}
		});
		
		action.getItems().add(pass);
		game.getItems().add(join);
		game.getItems().add(start);
		game.getItems().add(quit);
		menuBar.getMenus().add(game);
		menuBar.getMenus().add(action);
		
		return menuBar;
	}
	
	public void setUpGame(String color,String playersNumber, Group marbles) {
		
		int verseShift[] = {12,11,10,9,0,1,2,3,4,3,2,1,0,9,10,11,12};
		int firstInRow[] = {13,12,11,10,5,5,5,5,5,4,3,2,1,5,5,5,5};
		
		BoardGenerator generator = null;
		IRuleSet rules = new DefaultRuleSet();
		
		if(playersNumber.equals("2"))
			generator = new TwoPlayersBoardGenerator();

		else if(playersNumber.equals("3"))
			generator = new ThreePlayerBoardGenerator();

		else if(playersNumber.equals("4"))
			generator = new FourPlayersBoardGenerator();
		
		else if(playersNumber.equals("6"))
			generator = new SixPlayersBoardGenerator();		
	
		if(generator != null)
			board = generator.generateBoard(rules);
		
		fields = board.getFields();
		Marble marble;
		
		for(Field f : fields ) {
			
			int Row = f.getRow();
			int X = f.getX();
			int ShiftX = (X - firstInRow[Row-1]) * MARBLE_DIAMETER_SIZE  + verseShift[Row-1]*MARBLE_RADIUS_SIZE;
			int ShiftRow = (Row - 1) * MARBLE_DIAMETER_SIZE;
			
			marble = new Marble(X,Row,f.getColor().toString(),ShiftX,ShiftRow);
			
			marble.setOnMouseClicked(e -> {
					Marble tempMarble = (Marble) e.getSource();
					String marbleColor = tempMarble.getColor();
				
					if(!isWon && marbleColor.equals(playerColor) && tempMarble != oldField) {
						if(oldField != null) {
							oldField.setStrokeWidth(1);
							oldField.setStroke(Color.BLACK);
							oldField.setStrokeType(StrokeType.INSIDE);
							oldField = null;
						}
						
						oldField = (Marble) e.getSource();
						((Marble) e.getSource()).setStroke(Color.GOLD);
						((Marble) e.getSource()).setStrokeType(StrokeType.INSIDE);
						((Marble) e.getSource()).setStrokeWidth(3);
					}
					
					else if(oldField == tempMarble && !isWon) {
						oldField.setStrokeWidth(1);
						oldField.setStroke(Color.BLACK);
						oldField.setStrokeType(StrokeType.INSIDE);
						oldField = null;
					}
					
					else if(!isWon && oldField != null && oldField!=tempMarble && marbleColor != playerColor){
						newField = (Marble) e.getSource();
						if(newField!=null) {
							String command = "MOVE";
							command += " " + oldField.getX();
							command += " " + oldField.getRow();
							command += " " + newField.getX();
							command += " " + newField.getRow();
							clientOutput.println(command);
						}
					}
				});	
			marbles.getChildren().add(marble);
		}
	}
	
	public void moveOpponentsMarble(String coordinates) {
		
		String[] movingCoordinates = coordinates.split("\\s+");
		if(movingCoordinates.length != 6) {
			return;
		}
		
		String color = movingCoordinates[1];
		int oldX = 0;
		int oldRow = 0;
		int newX = 0;
		int newRow = 0;
		
		try {
			oldX = Integer.parseInt(movingCoordinates[2]);
			oldRow = Integer.parseInt(movingCoordinates[3]);
			newX = Integer.parseInt(movingCoordinates[4]);
			newRow = Integer.parseInt(movingCoordinates[5]);
		}
		catch(NumberFormatException ex) {}

		for(Node marble: marblesGroup.getChildren()) {
			if(((Marble) marble).getX() == oldX && ((Marble)marble).getRow()== oldRow) {
				((Marble) marble).setFill(Color.valueOf("#e4e4e4"));
			}
			else if(((Marble) marble).getX() == newX && ((Marble)marble).getRow()== newRow) {
				
				switch(color) {
					case "BLUE":
						((Marble) marble).setFill(Color.BLUE);	
						break;
					case "ORANGE":
						((Marble) marble).setFill(Color.ORANGE);
						break;
					case "BLACK":
						((Marble) marble).setFill(Color.BLACK);
						break;
					case "YELLOW":
						((Marble) marble).setFill(Color.YELLOW);
						break;
					case "RED":
						((Marble) marble).setFill(Color.RED);
						break;
					case "GREEN":
						((Marble) marble).setFill(Color.GREEN);
						break;
					default:
						break;
				}
			}
		}
	}
	
	public void printUserMessage(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				text.appendText(message);
			}
		});	
	}
}