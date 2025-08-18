package fin.starhud.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.config.ConditionalSettings;
import fin.starhud.helper.Box;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class AbstractHUD implements HUDInterface {

    protected final BaseHUDSettings baseHUDSettings;

    private int baseX;
    private int baseY;

    private int totalXOffset;
    private int totalYOffset;

    protected final Box boundingBox = new Box(-1, -1, -1, -1);

    public String groupId = null;

    public AbstractHUD(BaseHUDSettings baseHUDSettings) {
        this.baseHUDSettings = baseHUDSettings;
    }

    @Override
    public boolean shouldRender() {
        return getSettings().shouldRender();
    }

    // we update every HUD's x and y points here.
    @Override
    public void update() {
        baseX = getSettings().getCalculatedPosX();
        baseY = getSettings().getCalculatedPosY();

        if (!isInGroup()) {
            setXY(baseX + totalXOffset - getGrowthDirectionHorizontal(getWidth()), baseY + totalYOffset - getGrowthDirectionVertical(getHeight()));
            setScale(getSettings().getScale());
            clampPos();
        }
    }

    @Override
    public boolean appendDraw() {
        return drawHUD(getX(), getY(), shouldDrawBackground(), getScale());
    }

    @Override
    public boolean collect() {
        if (!collectHUDInformation())
            return false;

        modifyXY();

        setXY(baseX + totalXOffset - getGrowthDirectionHorizontal(getWidth()), baseY + totalYOffset - getGrowthDirectionVertical(getHeight()));
        setScale(getSettings().getScale());
        return true;
    }

    // collect what is needed for the hud to render.
    // the true purpose of collectData is to collect the width and height during data collection,
    // this is to ensure that the width and height can be used before the rendering
    // returns false if the HUD cannot be rendered
    // returns true if the HUD is ready to be rendered.
    public abstract boolean collectHUDInformation();

    // this is where the hud is rendered. Where we put the rendering logic.
    // it is highly discouraged to put information collecting in this function.
    // for information collecting please refer to collectHUDInformation()
    public abstract boolean drawHUD(int x, int y, boolean drawBackground, float scale);

    public abstract String getName();

    public void scaleHUD(DrawContext context) {
        float scaleFactor = getScale()  / (float) MinecraftClient.getInstance().getWindow().getScaleFactor();
        context.getMatrices().scale(scaleFactor, scaleFactor);
    }

    public void modifyXY() {
        int xOffset = 0, yOffset = 0;

        float scaleFactor = getSettings().getScaledFactor();
        for (ConditionalSettings condition : baseHUDSettings.getConditions()) {
            if (condition.renderMode != ConditionalSettings.RenderMode.HIDE && condition.isConditionMet()) {
                xOffset += condition.getXOffset(scaleFactor);
                yOffset += condition.getYOffset(scaleFactor);
            }
        }

        totalXOffset = xOffset;
        totalYOffset = yOffset;
    }

    public boolean isScaled() {
        return getScale() != 0 && (getScale() != MinecraftClient.getInstance().getWindow().getScaleFactor());
    }

    public boolean isInGroup() {
        return groupId != null;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public BaseHUDSettings getSettings() {
        return baseHUDSettings;
    }

    public int getGrowthDirectionHorizontal(int dynamicWidth) {
        return getSettings().getGrowthDirectionHorizontal(dynamicWidth);
    }

    public int getGrowthDirectionVertical(int dynamicHeight) {
        return getSettings().getGrowthDirectionVertical(dynamicHeight);
    }

    public boolean shouldDrawBackground() {
        return getSettings().drawBackground;
    }

    // bounding box attribute will return 0 if HUD is not rendered once.
    // the HUD must be rendered at least once to update the bounding box.

    public void setWidthHeight(int width, int height) {
        this.boundingBox.setWidthHeight(width, height);
    }

    public void setWidthHeightColor(int width, int height, int color) {
        this.boundingBox.setWidthHeightColor(width, height, color);
    }

    public void setXY(int x, int y) {
        this.boundingBox.setX(x);
        this.boundingBox.setY(y);
    }

    public void setScale(float scale) {
        this.boundingBox.setScale(scale);
    }

    public int getX() {
        return getBoundingBox().getX();
    }

    public int getY() {
        return getBoundingBox().getY();
    }

    public int getWidth() {
        return getBoundingBox().getWidth();
    }

    public int getHeight() {
        return getBoundingBox().getHeight();
    }

    public float getScale() {
        return getBoundingBox().getScale();
    }

    public Box getBoundingBox() {
        return boundingBox;
    }

    public void copyBoundingBox(Box boundingBox) {
        if (boundingBox != null)
            this.boundingBox.copyFrom(boundingBox);
    }

    public void setBoundingBox(int x, int y, int width, int height, int color) {
        this.boundingBox.setBoundingBox(x, y, width, height, color);
    }

    public void setBoundingBox(int x, int y, int width, int height) {
        this.boundingBox.setBoundingBox(x, y, width, height);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        if (isScaled()) {
            float scale = (float) MinecraftClient.getInstance().getWindow().getScaleFactor() / getScale();

            int scaledMouseX = (int) (mouseX * scale);
            int scaledMouseY = (int) (mouseY * scale);
            return scaledMouseX >= x && scaledMouseX <= x + width &&
                    scaledMouseY >= y && scaledMouseY <= y + height;
        } else {
            return (mouseX >= x && mouseX <= (x + width))
                    && (mouseY >= y && mouseY <= (y + height));
        }
    }

    public boolean intersects(int x1, int y1, int x2, int y2) {
        int hudLeft   = getX();
        int hudTop    = getY();
        int hudRight  = getX() + getWidth();
        int hudBottom = getY() + getHeight();

        if (isScaled()) {
            float scaleFactor = MinecraftClient.getInstance().getWindow().getScaleFactor() / getScale();

            int scaledX1 = (int) (x1 * scaleFactor);
            int scaledY1 = (int) (y1 * scaleFactor);
            int scaledX2 = (int) (x2 * scaleFactor);
            int scaledY2 = (int) (y2 * scaleFactor);

            return hudRight >= Math.min(scaledX1, scaledX2) &&
                    hudLeft  <= Math.max(scaledX1, scaledX2) &&
                    hudBottom >= Math.min(scaledY1, scaledY2) &&
                    hudTop    <= Math.max(scaledY1, scaledY2);
        } else {
            return hudRight >= Math.min(x1, x2) &&
                    hudLeft  <= Math.max(x1, x2) &&
                    hudBottom >= Math.min(y1, y2) &&
                    hudTop    <= Math.max(y1, y2);
        }
    }

    // dont go out of bounds please
    public void clampPos() {
        int scaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int scaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

        int x1 = 0;
        int y1 = 0;
        int x2 = scaledWidth;
        int y2 = scaledHeight;

        if (isScaled()) {
            float scaleFactor = MinecraftClient.getInstance().getWindow().getScaleFactor() / getScale();
            x1 = (int) (x1 * scaleFactor);
            y1 = (int) (y1 * scaleFactor);
            x2 = (int) (x2 * scaleFactor);
            y2 = (int) (y2 * scaleFactor);
        }

        int hudLeft   = getX();
        int hudTop    = getY();
        int hudRight  = hudLeft + getWidth();
        int hudBottom = hudTop + getHeight();

        int xOffset = 0, yOffset = 0;

        if (hudLeft < x1) {
            xOffset = x1 - hudLeft;
        } else if (hudRight > x2) {
            xOffset = x2 - hudRight;
        }

        if (hudTop < y1) {
            yOffset = y1 - hudTop;
        } else if (hudBottom > y2) {
            yOffset = y2 - hudBottom;
        }

        if (xOffset != 0 || yOffset != 0) {
            getSettings().x += xOffset;
            getSettings().y += yOffset;

            baseX = getSettings().getCalculatedPosX();
            baseY = getSettings().getCalculatedPosY();
            setXY(baseX + totalXOffset - getGrowthDirectionHorizontal(getWidth()), baseY + totalYOffset - getGrowthDirectionVertical(getHeight()));
        }
    }
}
