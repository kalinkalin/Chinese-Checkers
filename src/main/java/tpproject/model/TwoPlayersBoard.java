package tpproject.model;

import java.util.ArrayList;
import java.util.List;

public class TwoPlayersBoard extends AbstractBoard{

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
	           default:
	               generateCornerZone(zone, Color.NONE);
	               break;
	        }
	    }
}