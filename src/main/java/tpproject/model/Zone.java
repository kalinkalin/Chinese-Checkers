package tpproject.model;

public enum Zone {
    YELLOWZONE,
    REDZONE,
    BLACKZONE,
    GREENZONE,
    ORANGEZONE,
    BLUEZONE,
    CENTER;

    private Zone opposite;

    static {
        YELLOWZONE.opposite = REDZONE;
        REDZONE.opposite = YELLOWZONE;
        BLACKZONE.opposite = GREENZONE;
        GREENZONE.opposite = BLACKZONE;
        ORANGEZONE.opposite = BLUEZONE;
        BLUEZONE.opposite = ORANGEZONE;
        CENTER.opposite = CENTER;
    }

    public Zone getOppositeZone() {
        return opposite;
    }
}
