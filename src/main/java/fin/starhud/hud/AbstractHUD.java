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
    }

    @Override
    public void appendDraw() {
        setXY(baseX + totalXOffset - getGrowthDirectionHorizontal(getWidth()), baseY + totalYOffset - getGrowthDirectionVertical(getHeight()));
        setScale(getSettings().getScale());

        drawHUD(getX(), getY(), shouldDrawBackground(), getScale());
    }

    @Override
    public boolean collect() {
        if (!collectHUDInformation())
            return false;

        modifyXY();
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
}
