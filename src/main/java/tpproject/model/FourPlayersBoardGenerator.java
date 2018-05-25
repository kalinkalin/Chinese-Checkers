package tpproject.model;

public class FourPlayersBoardGenerator extends BoardGenerator{
	
	@Override
	public Board intantiateBoard() {
		FourPlayersBoard board = new FourPlayersBoard();
		return board;
	}
}