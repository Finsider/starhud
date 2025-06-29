package fin.starhud.helper;

public enum GrowthDirectionX {
    LEFT,
    CENTER,
    RIGHT;

    public int getGrowthDirection(int dynamicWidth) {
        return switch (this) {
            case LEFT -> dynamicWidth;
            case CENTER -> dynamicWidth / 2;
            case RIGHT -> 0;
        };
    }
}
