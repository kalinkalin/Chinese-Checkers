package tpproject.model;

public class TwoPlayersBoardGenerator extends BoardGenerator{
	
	@Override
	public Board intantiateBoard() {
		TwoPlayersBoard board = new TwoPlayersBoard();
		return board;
	}
}