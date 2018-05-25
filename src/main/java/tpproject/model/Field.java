package tpproject.model;

public class Field{
	
	private int row;
	private int x;
	private Color color;
	private Zone zone;
	
	public Field(int x, int row, Color color, Zone zone){
		
		this.x = x;
		this.row = row;
		this.color = color;
		this.zone = zone;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Zone getZone(){
		return zone;
	}
	
	public int getX(){
		return x;
	}
	
	public int getRow() {
		return row;
	}

}