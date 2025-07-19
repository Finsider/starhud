package fin.starhud.helper;

public class Box {
    private int x, y, width, height, color;

    public Box(int x, int y, int width, int height, int color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Box(int x, int y, int width, int height) {
        this(x, y, width, height, 0xFFFFFFFF);
    }

    public Box(int x, int y) {
        this(x, y, 13, 13, 0xFFFFFFFF);
    }

    public Box(int x, int y, int color) {
        this(x, y, 13, 13, color);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setEmpty(boolean empty) {
        if (empty) {
            setWidth(-1);
            setHeight(-1);
        } else {
            setWidth(13);
            setHeight(13);
        }
    }

    public boolean isEmpty() {
        return width < 0 || height < 0;
    }

    public void setBoundingBox(int x, int y, int width, int height, int color) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setColor(color);
    }

    public void setBoundingBox(int x, int y, int width, int height) {
        setBoundingBox(x, y, width, height, this.getColor());
    }

    public void copyFrom(Box other) {
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
        this.color = other.color;
    }

    // merge box essentially expanding the box area. not merging the color tho.
    public void mergeWith(Box other) {
        int minX = Math.min(this.getX(), other.getX());
        int minY = Math.min(this.getY(), other.getY());
        int maxX = Math.max(this.getX() + this.getWidth(), other.getX() + other.getWidth());
        int maxY = Math.max(this.getY() + this.getHeight(), other.getY() + other.getHeight());

        this.setX(minX);
        this.setY(minY);
        this.setWidth(maxX - minX);
        this.setHeight(maxY - minY);
    }

    @Override
    public String toString() {
        return "Box{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", color=" + color +
                '}';
    }
}
