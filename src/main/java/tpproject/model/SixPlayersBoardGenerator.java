package tpproject.model;

public class SixPlayersBoardGenerator extends BoardGenerator{
	
	@Override
	public Board intantiateBoard() {
		SixPlayersBoard board = new SixPlayersBoard();
		return board;
	}
}