package tpproject.model;

public class ThreePlayerBoardGenerator extends BoardGenerator{
	
	@Override
	public Board intantiateBoard() {
		ThreePlayersBoard board = new ThreePlayersBoard();
		return board;
	}
}