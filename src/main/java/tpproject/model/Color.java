package tpproject.model;

public enum Color {
    RED,
    YELLOW,
    GREEN,
    BLACK,
    BLUE,
    ORANGE,
    NONE;

    private Zone endingZone;

    static {
        RED.endingZone = Zone.REDZONE.getOppositeZone();
        YELLOW.endingZone = Zone.YELLOWZONE.getOppositeZone();
        GREEN.endingZone = Zone.GREENZONE.getOppositeZone();
        BLACK.endingZone = Zone.BLACKZONE.getOppositeZone();
        BLUE.endingZone = Zone.BLUEZONE.getOppositeZone();
        ORANGE.endingZone = Zone.ORANGEZONE.getOppositeZone();
        NONE.endingZone = Zone.CENTER.getOppositeZone();
    }

    public Zone getEndingZone() {
        return endingZone;
    }
}
