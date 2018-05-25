package tpproject.bot;

import tpproject.model.Field;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private int endValue;
    private List<Field> path = new ArrayList<>();
    private int currentPosition = 0;

    public int getEndValue() {
        return endValue;
    }

    public void setEndValue(int endValue) {
        this.endValue = endValue;
    }

    public void addNextField(Field f) {
        path.add(f);
    }

    public boolean isNextField() {
        if(currentPosition > path.size() - 1) {
            return false;
        }
        return true;
    }

    public Field getNextField() {
        Field f = path.get(currentPosition);
        currentPosition++;
        return f;
    }
}
