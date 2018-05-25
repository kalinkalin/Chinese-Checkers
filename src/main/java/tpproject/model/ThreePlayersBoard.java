package tpproject.model;

import java.util.ArrayList;

public class ThreePlayersBoard extends AbstractBoard{

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


	        switch (zone) {
	            case CENTER:
	                generateCenterZone();
	                break;
	            case REDZONE:
	                generateCornerZone(zone, Color.RED);
	                break;
	            case GREENZONE:
	                generateCornerZone(zone, Color.GREEN);
	                break;
	            case BLUEZONE:
	                generateCornerZone(zone, Color.BLUE);
	                break;
	            default:
	                generateCornerZone(zone, Color.NONE);
	                break;
	        }
	    }
}