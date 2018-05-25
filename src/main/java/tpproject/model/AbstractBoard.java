package tpproject.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBoard implements Board{
	
	protected ArrayList<Field> fields;
	protected IRuleSet rules;
	
    public void generateCenterZone() {
        int k = 0;

        for (int i = 5; i <= 13; i++) {                    // Wype�nienie dolnej cze�ci �rodka planszy
            for (int j = 9; j <= 13 - k; j++) 
                fields.add(new Field(i, j, Color.NONE, Zone.CENTER));

            if (i >= 9) 
                k++;
        }

        k = 0;

        for (int i = 13; i >= 6; i--) { 
        														// Wype�nienie g�rnej cz�ci �rodka planszy
            for (int j = 8; j >= 5 + k; j--) 
                fields.add(new Field(i, j, Color.NONE, Zone.CENTER));

            if (i <= 9)
                k++;
        }
    }
	
    public void generateCornerZone(Zone zone, Color color) {
        int x = 0;
        int row = 0;
        int deltaX = 0;
        int deltaRow = 0;

        switch (zone) {
        case REDZONE:
            x = 1;
            row = 13;
            deltaX = 1;
            deltaRow = -1;
            break;
        case BLACKZONE:
            x = 8;
            row = 5;
            deltaX = -1;
            deltaRow = 1;
            break;
        case BLUEZONE:
            x = 10;
            row = 4;
            deltaX = 1;
            deltaRow = -1;
            break;
        case YELLOWZONE:
            x = 17;
            row = 5;
            deltaX = -1;
            deltaRow = 1;
            break;
        case GREENZONE:
            x = 10;
            row = 13;
            deltaX = 1;
            deltaRow = -1;
            break;
        case ORANGEZONE:
            x = 8;
            row = 14;
            deltaX = -1;
            deltaRow = 1;
            break;
    }

        for (int i = x; i != x + 4 * deltaX; i += deltaX) {
            for (int j = row; j != row + (Math.abs(i - x) + 1) * deltaRow; j += deltaRow) {
                fields.add(new Field(i, j, color, zone));
            }
        }
    }
    
    @Override
    public boolean checkWin( Color winningColor) {

            if(fields.stream().filter(f -> f.getZone().equals(winningColor.getEndingZone())).allMatch(f -> f.getColor().equals(winningColor))) {
                return true;
        }

        return false;
    }

    @Override
    public void move(int oldX, int oldRow, int newX, int newRow,Color movingColor) throws WrongMoveException {
        Field oldField = fields.stream().filter(f -> f.getX() == oldX && f.getRow() == oldRow).findFirst().orElse(null);    // znajdowanie starego pola
        Field newField = fields.stream().filter(f -> f.getX() == newX && f.getRow() == newRow).findFirst().orElse(null);    // znajdowanie nowego pola
        
        if(!isValidMove(oldField, newField,movingColor)) {
            throw new WrongMoveException();
        }

        newField.setColor(oldField.getColor());
        oldField.setColor(Color.NONE);
    }
    
    @Override
    public boolean isValidMove(Field oldField, Field newField, Color movingColor) {
        return rules.isValidMove(oldField, newField, movingColor);
    }
    
    @Override
    public boolean isAnotherMoveAvailable(int x, int row, Field jumpedField) {
        return rules.isAnotherMoveAvailable(x, row, jumpedField);
    }

    @Override
    public Field getFieldAt(int x, int row) {
        return fields.stream().filter(f -> f.getX() == x && f.getRow() == row).findFirst().orElse(null);
    }
    
    @Override
    public void temporaryPrint() {
        fields.forEach(System.out::println);
        System.out.println(fields.size());
    }

    @Override
    public List<Field> getFields(){
        return fields;
    }
   
    @Override
    public void setAnotherMoveValue(boolean isAnotherMove) {
        rules.setAnotherMoveValue(isAnotherMove);
    }

    @Override
    public boolean getAnotherMoveValue() {
        return rules.getAnotherMoveValue();
    }
    
}