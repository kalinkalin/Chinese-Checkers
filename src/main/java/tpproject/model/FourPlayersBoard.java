package tpproject.model;

import java.util.ArrayList;

public class FourPlayersBoard extends AbstractBoard{

	@Override
	public void generateBoard(IRuleSet rules) {
		
		fields = new ArrayList<Field>();
		this.rules = rules;
		this.rules.setBoard(this);
		
		for(Zone z : Zone.values()) {
            generateZone(z);
        }
	}

	  public void generateZone(Zone zone) {


	        switch(zone) {
	            case CENTER:
	                generateCenterZone();
	                break;
	            case REDZONE:
	                generateCornerZone(zone, Color.RED);
	                break;
	            case YELLOWZONE:
	                generateCornerZone(zone, Color.YELLOW);
	                break;
	            case BLUEZONE:
	                generateCornerZone(zone, Color.BLUE);
	                break;
	            case ORANGEZONE:
	                generateCornerZone(zone, Color.ORANGE);
	                break;
	            default:
	            	generateCornerZone(zone, Color.NONE);
	                break;
	        }
	    }
}