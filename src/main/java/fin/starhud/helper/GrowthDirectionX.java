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

    public GrowthDirectionX next() {
        return switch (this) {
            case LEFT -> CENTER;
            case CENTER -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    public GrowthDirectionX prev() {
        return switch (this) {
            case LEFT -> RIGHT;
            case CENTER -> LEFT;
            case RIGHT -> CENTER;
        };
    }

    public GrowthDirectionX recommendedScreenAlignment(ScreenAlignmentX screenAlignmentX) {
        return switch (screenAlignmentX) {
            case LEFT -> RIGHT;
            case CENTER -> CENTER;
            case RIGHT -> LEFT;
        };
    }
}
