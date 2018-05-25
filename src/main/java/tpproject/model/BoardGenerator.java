package tpproject.model;


public abstract class BoardGenerator{
	
	public Board generateBoard(IRuleSet rules) {
		Board generatedBoard = intantiateBoard();
		generatedBoard.generateBoard(rules);
		return generatedBoard;
	}
	
	public abstract Board intantiateBoard();
}