package tpproject.server;

import java.net.Socket;
import tpproject.model.Color;

public interface Observer{
	
	public void updateObserver(String message);
	
	public Color getColor();
	public void setColor(Color color);
	public Socket getSocket();
	
}