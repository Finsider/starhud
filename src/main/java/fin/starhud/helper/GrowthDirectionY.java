package fin.starhud.helper;

public enum GrowthDirectionY {
    UP,
    MIDDLE,
    DOWN;

    public int getGrowthDirection(int dynamicHeight) {
        return switch (this) {
            case UP -> dynamicHeight;
            case MIDDLE -> dynamicHeight / 2;
            case DOWN -> 0;
        };
    }
}
