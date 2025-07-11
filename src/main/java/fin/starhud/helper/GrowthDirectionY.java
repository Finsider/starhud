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

    public GrowthDirectionY next() {
        return switch (this) {
            case UP -> MIDDLE;
            case MIDDLE -> DOWN;
            case DOWN -> UP;
        };
    }

    public GrowthDirectionY prev() {
        return switch (this) {
            case UP -> DOWN;
            case MIDDLE -> UP;
            case DOWN -> MIDDLE;
        };
    }
}
