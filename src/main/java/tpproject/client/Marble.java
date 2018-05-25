package tpproject.client;
import javafx.scene.shape.*;
import javafx.scene.paint.*;

public class Marble extends Ellipse{

	private int X;
	private int Row;
	private String marbleColor;
	
	Marble(int X, int Row, String marbleColor, int ShiftX, int ShiftRow){
		
		this.X = X;
		this.Row = Row;
		this.marbleColor = marbleColor;
		
		setRadiusX(Client.MARBLE_RADIUS_SIZE-3);
		setRadiusY(Client.MARBLE_RADIUS_SIZE-3);
		relocate(ShiftX+2,ShiftRow+2);
		
		if(marbleColor == "RED") {
			setFill(Color.RED);
		}
		else if(marbleColor == "YELLOW") {
			setFill(Color.YELLOW);
		}
		else if(marbleColor == "BLUE") {
			setFill(Color.BLUE);
		}
		else if(marbleColor == "NONE") {
			setFill(Color.valueOf("#e4e4e4"));
		}
		else if(marbleColor == "ORANGE") {
			setFill(Color.ORANGE);
		}
		else if(marbleColor == "GREEN") {
			setFill(Color.GREEN);
		}
		else if(marbleColor == "BLACK") {
			setFill(Color.BLACK);
		}
		
		setStroke(Color.BLACK);
		setStrokeWidth(1);
		setStrokeType(StrokeType.INSIDE);
	}
	
	public int getX() {
		return X;
	}
	public int getRow() {
		return Row;
	}
	
	public String getColor() {
		return marbleColor;
	}
	public void setColor(String color) {
		this.marbleColor = color;
	}
}
