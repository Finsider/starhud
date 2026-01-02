package fin.starhud.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.config.ConditionalSettings;
import fin.starhud.helper.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class AbstractHUD implements HUDInterface {

    protected final BaseHUDSettings baseHUDSettings;

    private int baseX;
    private int baseY;

    private int totalXOffset;
    private int totalYOffset;

    private int startDragX;
    private int startDragY;
    private ScreenAlignmentX startDragAlignmentX;
    private ScreenAlignmentY startDragAlignmentY;
    private GrowthDirectionX startDragGrowthX;
    private GrowthDirectionY startDragGrowthY;

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
            updatePos();
        }
    }

    @Override
    public boolean render(DrawContext context) {
        if (!isScaled())
            return renderHUD(context, getX(), getY(), shouldDrawBackground());

        // this is so we can change the scale for one hud but not the others.
        context.getMatrices().pushMatrix();
        scaleHUD(context);

        boolean result = renderHUD(context, getX(), getY(), shouldDrawBackground());
        context.getMatrices().popMatrix();

        return result;
    }

    @Override
    public boolean collect() {
        if (!collectHUDInformation())
            return false;

        modifyXY();

        updatePos();
        return true;
    }

    // collect what is needed for the hud to render.
    // the true purpose of collectHUDInformation is to collect the width and height during data collection,
    // this is to ensure that the width and height can be used before the rendering
    // returns false if the HUD cannot be rendered
    // returns true if the HUD is ready to be rendered.
    public abstract boolean collectHUDInformation();

    // this is where the hud is rendered. Where we put the rendering logic.
    // it is highly discouraged to put information collecting in this function.
    // for information collecting please refer to collectHUDInformation()
    public abstract boolean renderHUD(DrawContext context, int x, int y, boolean drawBackground);

    public abstract String getName();

    public void scaleHUD(DrawContext context) {
        float scaleFactor = getScale();
        context.getMatrices().translate(getX(), getY());
        context.getMatrices().scale(scaleFactor, scaleFactor);
        context.getMatrices().translate(-getX(), -getY());
    }

    public void updatePos() {
        setScale(getSettings().getScale());
        setXY(baseX + totalXOffset - getGrowthDirectionHorizontal((int) (getWidth() * getScale())), baseY + totalYOffset - getGrowthDirectionVertical((int) (getHeight() * getScale())));
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
        return true;
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

    public int getTrueWidth() {
        return (int) (getWidth() * getScale());
    }

    public int getTrueHeight() {
        return (int) (getHeight() * getScale());
    }

    public int getColor() {
        return getBoundingBox().getColor();
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

    public void setStartDragX(int startDragX) {
        this.startDragX = startDragX;
    }

    public void setStartDragY(int startDragY) {
        this.startDragY = startDragY;
    }

    public void setStartDragAlignmentX(ScreenAlignmentX startDragAlignmentX) {
        this.startDragAlignmentX = startDragAlignmentX;
    }

    public void setStartDragAlignmentY(ScreenAlignmentY startDragAlignmentY) {
        this.startDragAlignmentY = startDragAlignmentY;
    }

    public void setStartDragGrowthX(GrowthDirectionX startDragGrowthX) {
        this.startDragGrowthX = startDragGrowthX;
    }

    public void setStartDragGrowthY(GrowthDirectionY startDragGrowthY) {
        this.startDragGrowthY = startDragGrowthY;
    }

    public int getStartDragX() {
        return this.startDragX;
    }

    public int getStartDragY() {
        return this.startDragY;
    }

    public ScreenAlignmentX getStartDragAlignmentX() {
        return startDragAlignmentX;
    }

    public ScreenAlignmentY getStartDragAlignmentY() {
        return startDragAlignmentY;
    }

    public GrowthDirectionX getStartDragGrowthX() {
        return startDragGrowthX;
    }

    public GrowthDirectionY getStartDragGrowthY() {
        return startDragGrowthY;
    }

    public void setupStartDrag() {
        setStartDragX(getSettings().getX());
        setStartDragY(getSettings().getY());
        setStartDragAlignmentX(getSettings().getOriginX());
        setStartDragAlignmentY(getSettings().getOriginY());
        setStartDragGrowthX(getSettings().getGrowthDirectionX());
        setStartDragGrowthY(getSettings().getGrowthDirectionY());
    }

    public boolean isHovered(double mouseX, double mouseY) {
        final float scale = MinecraftClient.getInstance().getWindow().getScaleFactor();

        mouseX = (int) (mouseX * scale);
        mouseY = (int) (mouseY * scale);

        int x = getX();
        int y = getY();
        int width = getTrueWidth();
        int height = getTrueHeight();

        return (mouseX >= x && mouseX <= (x + width))
                && (mouseY >= y && mouseY <= (y + height));

    }

    public boolean intersects(int x1, int y1, int x2, int y2) {
        final float scale = MinecraftClient.getInstance().getWindow().getScaleFactor();

        x1 = (int) (x1 * scale);
        y1 = (int) (y1 * scale);
        x2 = (int) (x2 * scale);
        y2 = (int) (y2 * scale);

        int hudLeft   = getX();
        int hudTop    = getY();
        int hudRight  = getX() + getTrueWidth();
        int hudBottom = getY() + getTrueHeight();

        return hudRight >= Math.min(x1, x2) &&
                hudLeft  <= Math.max(x1, x2) &&
                hudBottom >= Math.min(y1, y2) &&
                hudTop    <= Math.max(y1, y2);
    }

    // dont go out of bounds please
    public boolean clampPos() {
        int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
        int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

        int x1 = 0;
        int y1 = 0;
        int x2 = windowWidth;
        int y2 = windowHeight;

        int hudLeft   = getX();
        int hudTop    = getY();
        int hudRight  = hudLeft + getTrueWidth();
        int hudBottom = hudTop + getTrueHeight();

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
            updatePos();

            return true;
        }

        return false;
    }
}
