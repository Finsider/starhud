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

    // IF RIGHT, shift hud to the left a bit so that no pixel is leaving the screen (supposed you have set x:0, y:0)
    public int getTextureOffset(int textureWidth) {
        return switch (this) {
            case LEFT -> 0;
            case CENTER -> textureWidth / 2;
            case RIGHT -> textureWidth;
        };
    }
}
