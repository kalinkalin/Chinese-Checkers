package tpproject.bot;

import tpproject.model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Bot extends Thread {
    private int serverPort = 9090;
    private Socket botSocket;
    private BufferedReader botInput;
    private PrintWriter botOutput;
    private Color botColor;
    private Path path;
    private Board board;

    @Override
    public void run() {
        connectToServer();
        String command = "";

        try {
            botOutput.println("JOIN");
            command = botInput.readLine();
            if (!command.equals("JOINED")) {
                return;
            }
            while (!command.equals("GAME_STARTED")) {
                command = botInput.readLine();
            }

            String color = botInput.readLine();
            String number = botInput.readLine();
            prepareBoard(color, number);

        }
        catch(IOException ex) {
            System.err.println("Cannot connect to the server!");
        }

        while(!command.startsWith("WIN")) {
            try {
                command = botInput.readLine();
                if(command.equals("MOVE")) {
                    makeMove();
                }
                else if(command.equals("MOVE_AGAIN")) {
                    makeAnotherMove();
                }
                else if(command.equals("WRONG_MOVE")) {
                    makeSafeMove();
                }
                else if(command.startsWith("MOVED")) {
                    moveOpponents(command);
                }
            }
            catch(IOException ex) {}
        }
    }

    public void connectToServer() {
        try {
            botSocket = new Socket("localhost", serverPort);
            botInput = new BufferedReader(new InputStreamReader(botSocket.getInputStream()));
            botOutput = new PrintWriter(botSocket.getOutputStream(), true);
        }
        catch (IOException ex) {
            System.err.println("Cannot connect to the server!");
        }
    }

    public void prepareBoard(String color, String number) {
    	BoardGenerator generator;
        switch (number) {
            case "2":
                generator = new TwoPlayersBoardGenerator();
                break;
            case "3":
                generator = new ThreePlayerBoardGenerator();
                break;
            case "4":
                generator = new FourPlayersBoardGenerator();
                break;
            case "6":
                generator = new SixPlayersBoardGenerator();
                break;
            default:
                System.err.println("Wrong number of players!");
                throw new IllegalArgumentException();
        }
        
        if(generator != null) {
            botColor = Color.valueOf(color);
            board = generator.generateBoard(new DefaultRuleSet());
        }
    }

    public void makeMove() {
        int currentState = evaluateState(board.getFields());

        List<Field> fields = copyBoardFields();
        List<Path> paths = new ArrayList<>();
        List<Field> botFields = fields.stream().filter(f -> f.getColor().equals(botColor)).collect(Collectors.toList());

        for(Field f : botFields) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    final int deltaX = i;
                    final int deltaRow = j;
                    Field newField = fields.stream().filter(field -> field.getX() == f.getX() + deltaX && field.getRow() == f.getRow() + deltaRow).findFirst().orElse(null);
                    if(board.isValidMove(f, newField, botColor)) {
                        Path newPath = new Path();
                        newPath.addNextField(f);
                        newPath.addNextField(newField);

                        //TODO dluzsze skoki

                        newField.setColor(f.getColor());
                        f.setColor(Color.NONE);

                        newPath.setEndValue(evaluateState(fields));

                        paths.add(newPath);

                        f.setColor(newField.getColor());
                        newField.setColor(Color.NONE);
                        board.setAnotherMoveValue(false);
                    }
                }
            }
        }
        board.setAnotherMoveValue(false);

        path = null;
        for(Path p : paths) {
            if(path != null && p.getEndValue() < path.getEndValue()) {
                path = p;
            }
            else if(path == null && p.getEndValue() < currentState) {
                path = p;
            }
        }

        if(path == null) {
            botOutput.println("PASS");
        }
        else {
            Field startField = path.getNextField();
            Field nextField = path.getNextField();

            try {
                board.move(startField.getX(), startField.getRow(), nextField.getX(), nextField.getRow(), botColor);
            }
            catch(WrongMoveException ex) {}

            String command = "MOVE";
            command += " " + startField.getX();
            command += " " + startField.getRow();
            command += " " + nextField.getX();
            command += " " + nextField.getRow();

            botOutput.println(command);
        }
    }

    public void makeSafeMove() {
        List<Field> fields = copyBoardFields();
        List<Path> paths = new ArrayList<>();

        List<Field> botFields = fields.stream().filter(f -> f.getColor().equals(botColor)).collect(Collectors.toList());

        for (Field f : botFields) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    final int deltaX = i;
                    final int deltaRow = j;
                    Field newField = fields.stream().filter(field -> field.getX() == f.getX() + deltaX && field.getRow() == f.getRow() + deltaRow).findFirst().orElse(null);

                    Path newPath = new Path();
                    newPath.addNextField(f);
                    newPath.addNextField(newField);

                    paths.add(newPath);
                }
            }
        }

        boolean isNotValid = true;
        Field startField = null;
        Field nextField = null;

        while (isNotValid) {
            int index = new Random().nextInt(paths.size());
            startField = paths.get(index).getNextField();
            nextField = paths.get(index).getNextField();

            try {
                board.move(startField.getX(), startField.getRow(), nextField.getX(), nextField.getRow(), botColor);
                isNotValid = false;
            } catch (WrongMoveException ex) { }
        }

        String command = "MOVE";
        command += " " + startField.getX();
        command += " " + startField.getRow();
        command += " " + nextField.getX();
        command += " " + nextField.getRow();

        botOutput.println(command);
    }

    public void makeAnotherMove() {
        makeMove();
    }

    public void moveOpponents(String command) {
        String[] commandSplitted = command.split(" ");
        int[] coordinates = {Integer.parseInt(commandSplitted[2]), Integer.parseInt(commandSplitted[3]), Integer.parseInt(commandSplitted[4]), Integer.parseInt(commandSplitted[5])};
        try {
            board.move(coordinates[0], coordinates[1], coordinates[2], coordinates[3], Color.valueOf(commandSplitted[1]));
        }
        catch(WrongMoveException ex) {}
    }

    public static void main(String[] args) {
        new Bot().start();
    }

    private List<Field> copyBoardFields() {
        List<Field> fields = new ArrayList<>();
        board.getFields().forEach(f -> fields.add(new Field(f.getX(), f.getRow(), f.getColor(), f.getZone())));

        return fields;
    }

    private int evaluateState(List<Field> fields) {
        int cornerX = -1;
        int cornerRow = -1;

        switch(botColor) {
            case YELLOW:
                cornerX = 1;
                cornerRow = 13;
                break;
            case BLUE:
                cornerX = 8;
                cornerRow = 14;
                break;
            case RED:
                cornerX = 17;
                cornerRow = 5;
                break;
            case ORANGE:
                cornerX = 10;
                cornerRow = 4;
                break;
            case GREEN:
                cornerX = 8;
                cornerRow = 5;
                break;
            case BLACK:
                cornerX = 10;
                cornerRow = 13;
                break;
        }

        final int cX = cornerX;
        final int cRow = cornerRow;

        Field corner = fields.stream().filter(f -> f.getX() == cX && f.getRow() == cRow).findFirst().orElse(null);

        int value = 0;
        List<Field> botFields = fields.stream().filter(f -> f.getColor().equals(botColor)).collect(Collectors.toList());

        for(Field f : botFields) {
            int distance = calculateFieldsDistance(corner, f);
            value += distance*distance;
        }

        return value;
    }

    private int calculateFieldsDistance(Field f1, Field f2) {
        int deltaX = Math.abs(f1.getX() - f2.getX());
        int deltaRow = Math.abs(f1.getRow() - f2.getRow());

        return (int)(Math.sqrt(deltaX*deltaX + deltaRow*deltaRow));
    }
}
