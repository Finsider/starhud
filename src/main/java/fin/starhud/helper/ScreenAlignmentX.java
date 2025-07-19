package fin.starhud.helper;

public enum ScreenAlignmentX {
    LEFT,
    CENTER,
    RIGHT;

    // adjust position based on alignment
    // if RIGHT, place HUD on the right side of the screen, basically.
    public int getAlignmentPos(int scaledWidth) {
        return switch (this) {
            case LEFT -> 0;
            case CENTER -> scaledWidth / 2;
            case RIGHT -> scaledWidth;
        };
    }

    public ScreenAlignmentX next() {
        return switch (this) {
            case LEFT -> CENTER;
            case CENTER -> RIGHT;
            case RIGHT -> LEFT;
        };
    }

    public ScreenAlignmentX prev() {
        return switch (this) {
            case LEFT -> RIGHT;
            case CENTER -> LEFT;
            case RIGHT -> CENTER;
        };
    }
}
