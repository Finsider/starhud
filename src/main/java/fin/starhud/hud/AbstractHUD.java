package fin.starhud.hud;

import fin.starhud.config.BaseHUDSettings;
import fin.starhud.config.ConditionalSettings;
import fin.starhud.helper.Box;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;


public abstract class AbstractHUD implements HUDInterface {
    private static final Window WINDOW = MinecraftClient.getInstance().getWindow();

    protected final BaseHUDSettings baseHUDSettings;

    // the actual x and y point we use in HUDs
    protected int x;
    protected int y;

    // temporary x and y point.
    protected int baseX;
    protected int baseY;

    protected final Box boundingBox = new Box(0, 0);

    public AbstractHUD(BaseHUDSettings baseHUDSettings) {
        this.baseHUDSettings = baseHUDSettings;
    }

    @Override
    public boolean shouldRender() {
        return baseHUDSettings.shouldRender && shouldRenderOnCondition();
    }

    // we update every HUD's x and y points here.
    @Override
    public void update() {
        updateX();
        updateY();
        setBoundingBox(getBaseX(), getBaseY(), getBaseHUDWidth(), getBaseHUDHeight());
    }

    @Override
    public boolean render(DrawContext context) {
        // I hate this piece of code
        // if Condition is triggered, the X will be modified with xOffset on that condition.
        // example: when bossbar is present, we want to move our hud under the bossbar, or avoid the bossbar.
        // modifyX and modifyY will add our xOffset and yOffset to our initial x position.
        modifyXY();

        // if the HUD' scale is set to default, don't... change the scale...? whatever, this is faster than the one below.
        if (!isScaled()) {
            return renderHUD(context);
        }

        // this is so we can change the scale for one hud but not the others.
        context.getMatrices().pushMatrix();
        setHUDScale(context);

        try {
            return renderHUD(context);
        } finally {
            context.getMatrices().popMatrix();
        }
    }

    public abstract boolean renderHUD(DrawContext context);
    public abstract int getBaseHUDWidth();
    public abstract int getBaseHUDHeight();
    public abstract String getName();

    public void setHUDScale(DrawContext context) {
        float scaleFactor = baseHUDSettings.scale / (float) WINDOW.getScaleFactor();
        context.getMatrices().scale(scaleFactor, scaleFactor);
    }

    public void modifyXY() {
        int tempX = 0, tempY = 0;

        for (ConditionalSettings condition : baseHUDSettings.conditions) {
            if (condition.isConditionMet()) {
                tempX += condition.xOffset;
                tempY += condition.yOffset;
            }
        }

        x = baseX + tempX;
        y = baseY + tempY;
    }

    public void updateX() {
        baseX = baseHUDSettings.getCalculatedPosX() - baseHUDSettings.growthDirectionX.getGrowthDirection(getBaseHUDWidth());
        x = baseX;
    }

    public void updateY() {
        baseY = baseHUDSettings.getCalculatedPosY() - baseHUDSettings.growthDirectionY.getGrowthDirection(getBaseHUDHeight());
        y = baseY;
    }

    public boolean shouldRenderOnCondition() {
        for (ConditionalSettings condition: baseHUDSettings.conditions) {
            if (!condition.shouldRender && condition.isConditionMet())
                return false;
        }
        return true;
    }

    public BaseHUDSettings getSettings() {
        return this.baseHUDSettings;
    }

    public int getBaseX() {
        return baseX;
    }

    public int getBaseY() {
        return baseY;
    }

    public boolean isScaled() {
        return this.baseHUDSettings.scale != 0 || (this.baseHUDSettings.scale / (double) WINDOW.getScaleFactor()) == 1;
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
