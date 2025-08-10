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
        setXY(baseX + totalXOffset - getGrowthDirectionHorizontal(getWidth()), baseY + totalYOffset - getGrowthDirectionVertical(getHeight()));
    }

    @Override
    public boolean render(DrawContext context) {
        if (!isScaled())
            return renderHUD(context, getX(), getY(), shouldDrawBackground());

        // this is so we can change the scale for one hud but not the others.
        context.getMatrices().push();
        setHUDScale(context);

        try {
            return renderHUD(context, getX(), getY(), shouldDrawBackground());
        } finally {
            context.getMatrices().pop();
        }
    }

    @Override
    public boolean collect() {
        if (!collectHUDInformation())
            return false;

        modifyXY();

        setXY(baseX + totalXOffset - getGrowthDirectionHorizontal(getWidth()), baseY + totalYOffset - getGrowthDirectionVertical(getHeight()));
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
    public abstract boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground);

    public abstract String getName();

    public void setHUDScale(DrawContext context) {
        float scaleFactor = getSettings().getScale() / (float) MinecraftClient.getInstance().getWindow().getScaleFactor();
        context.getMatrices().scale(scaleFactor, scaleFactor, scaleFactor);
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
        return this.getSettings().getScale() != 0 && (this.getSettings().getScale() != MinecraftClient.getInstance().getWindow().getScaleFactor());
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
}
