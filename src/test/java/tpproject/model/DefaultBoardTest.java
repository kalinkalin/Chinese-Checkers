package tpproject.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DefaultBoardTest
{
	private BoardGenerator generator;
	private IRuleSet rules;
    private Board db;
    private Field field1;
    private Field field2;

    @Before
    public void setUp() {
    	rules = new DefaultRuleSet();
    	generator = new SixPlayersBoardGenerator();
        db = generator.generateBoard(rules);
    }

    @Test
    public void shouldNotBeWinnerOnTheBeginningOfTheGame() {
        assertEquals("There is a winner on the beginning of the game!", db.checkWin(Color.RED), false);
    }
    
    @Test
    public void shouldBeWinnerIfMarblesInRightZone() {

    	db.getFields()
    			.forEach(
    					f -> {
    						if(f.getZone().equals(Color.YELLOW.getEndingZone())) {
    							f.setColor(Color.YELLOW);
    						}
    					}
    	);

    	assertEquals("No winner if marbles are in the right zone",db.checkWin(Color.YELLOW),true);
    }
    
    @Test
    public void shouldMoveToEmptyNeighbour() throws WrongMoveException {
    	db.move(7, 14, 8, 13,Color.ORANGE);
    	field1 = db.getFields().stream().filter(f -> f.getX() == 7 && f.getRow() == 14).findFirst().orElse(null);
    	field2 = db.getFields().stream().filter(f -> f.getX() == 8 && f.getRow() == 13).findFirst().orElse(null);
    	assertEquals("First field didn't change",Color.NONE,field1.getColor());
    	assertEquals("Second field didn't change",Color.ORANGE,field2.getColor());
    }
    
    @Test(expected = WrongMoveException.class)
    public void shouldNotJumpOverMoreThenTwoFields() throws WrongMoveException {
    	db.move(7, 14, 8, 13, Color.ORANGE);
    	db.move(5, 16, 9, 12, Color.ORANGE);
    	fail("Marble can jump over more then two fields at once");
    }
    
    @Test(expected = WrongMoveException.class)
    public void shouldNotMoveThroughTheEmptyFields() throws WrongMoveException {
    	db.move(7, 14, 9, 12, Color.ORANGE);
    	fail("moving through the empty fields is permitted");
    }
     
    @Test(expected = WrongMoveException.class)
    public void shouldNotMoveOnTheOccupiedFields() throws WrongMoveException {
    	db.move(5,16,7,14,Color.ORANGE);
    	fail("moving on the occupied fields is permitted");
    }
    
    @Test
    public void shouldJumpOverTheOpponentMarbles() throws WrongMoveException {
    	db.move(7, 14, 8, 13, Color.ORANGE);
    	field1 = db.getFields().stream().filter(f -> f.getX() == 7 && f.getRow() == 14).findFirst().orElse(null);
    	field2 = db.getFields().stream().filter(f -> f.getX() ==6 && f.getRow() == 15).findFirst().orElse(null);
    	assertEquals("Field color didnt change",field1.getColor(), Color.NONE);
    	assertEquals("Field color, didnt change",field2.getColor(), Color.ORANGE);
 
    }
    
    @Test(expected = WrongMoveException.class)
    public void shouldNotMoveOutsideTheBoard() throws WrongMoveException {
    	field1 = db.getFields().stream().filter(f -> f.getX() == 2 && f.getRow() == 13).findFirst().orElse(null);
    	field1.setColor(Color.NONE);
    	db.move(5, 16, 2, 13,Color.ORANGE);
    	fail("Moving outside the board is permitted");
    }
    
    @Test
    public void shouldJumpOverOneMarble() throws WrongMoveException {
    	db.move(6, 15, 8, 13,Color.ORANGE);
    	field1 = db.getFields().stream().filter(f -> f.getX() == 6 && f.getRow() == 15).findFirst().orElse(null);
    	field2 = db.getFields().stream().filter(f -> f.getX() ==8 && f.getRow() == 13).findFirst().orElse(null);
    	assertEquals("First field didn't change",Color.NONE, field1.getColor());
    	assertEquals("Second field didn't change",Color.ORANGE, field2.getColor());
    }
    
    @Test (expected = WrongMoveException.class)
    public void shouldNotMoveThroughWrongPath() throws WrongMoveException {
    	db.move(12,4,10,7,Color.ORANGE);
    	fail("Marble can move through the wrong paths");
    }
    
    @Test(expected = WrongMoveException.class)
    public void shouldNotMoveOneFieldIfAnotherMove() throws WrongMoveException {
    	db.setAnotherMoveValue(true);
    	db.move(7, 14, 8, 13,Color.BLUE);
    	fail("Marble can move to empty field on second move");
    }
    
    @Test
    public void shouldNotBeAvaliableToMoveBack() throws WrongMoveException{
    	field1 = db.getFields().stream().filter(f -> f.getX() == 7 && f.getRow() == 9).findFirst().orElse(null);
    	field1.setColor(Color.BLUE);
    	field2 = db.getFields().stream().filter(f -> f.getX() == 7 && f.getRow() == 10).findFirst().orElse(null);
    	field2.setColor(Color.BLACK);
    	db.move(7,10,7,8,Color.BLACK);
    	assertEquals("Marbles are avaliable to move back",false,db.getAnotherMoveValue());
    }
    
    @Test
    public void shouldNotBeAvaliableToMoveAgain() throws WrongMoveException {
    	field1 = db.getFields().stream().filter(f -> f.getX() == 7 && f.getRow() == 9).findFirst().orElse(null);
    	field1.setColor(Color.BLUE);
    	field2 = db.getFields().stream().filter(f -> f.getX() == 7 && f.getRow() == 11).findFirst().orElse(null);
    	field2.setColor(Color.BLACK);
    	
    	db.move(7, 11,7,10,Color.BLACK);
    	assertEquals("Moving on and jumping is perimtted", db.getAnotherMoveValue(),false);
    }
    
    @Test
    public void shouldBeAvaliableToMoveAgain() throws WrongMoveException {
    	db.move(7, 14, 8, 13,Color.ORANGE);
    	db.move(5, 16, 7, 14,Color.ORANGE);
    	assertEquals("Marble cannot jump through exploded opposite marbles",db.getAnotherMoveValue(), true);
    	db.move(7, 14, 9, 12,Color.ORANGE);
    	assertEquals("Marble can move back",db.getAnotherMoveValue(), false);
    }
    
    @Test 
    public void shouldNotBeAvaliableToMoveAgainThroughOccupiedFields() throws WrongMoveException {
    	db.move(7, 15, 7, 13,Color.ORANGE);
    	assertEquals("Marble can move through occupied fields in second move",false,db.getAnotherMoveValue());
    }
    
    @Test
    public void shouldBeAvaliableToMoveAgainWhileMultipleMoveOptions() throws WrongMoveException {
    	field1 = db.getFields().stream().filter(f -> f.getX() == 7 && f.getRow() ==11).findFirst().orElse(null);
    	field1.setColor(Color.ORANGE);
    	field2 = db.getFields().stream().filter(f -> f.getX() == 9 && f.getRow() ==11).findFirst().orElse(null);
    	field2.setColor(Color.YELLOW);
    	Field field3 = db.getFields().stream().filter(f -> f.getX() ==8 && f.getRow() ==12).findFirst().orElse(null);
    	field3.setColor(Color.BLACK);
    	Field field4 = db.getFields().stream().filter(f -> f.getX() == 8 && f.getRow() == 13).findFirst().orElse(null);
    	field4.setColor(Color.GREEN);
    	
    	db.move(8, 13, 8, 11,Color.GREEN);
    	assertEquals("Marble cannot move in second move if there are multiple options",db.getAnotherMoveValue(),true);
    }
}
