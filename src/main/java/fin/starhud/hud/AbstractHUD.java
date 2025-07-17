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
        return getSettings().shouldRender();
    }

    // we update every HUD's x and y points here.
    @Override
    public void update() {
        updateX();
        updateY();
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

    public abstract String getName();
    public abstract boolean renderHUD(DrawContext context);

    /*
    * Base HUD Width / Height
    * the width of the hud that is not affected by any realtime changing length
    *
    * Example: Biome HUD, since the name of biome can be of any length, we can't exactly have a "static" length,
    *          hence the base hud width is (ICON_WIDTH + GAP + TEXT_PADDING_LEFT + TEXT_PADDING_RIGHT)
    *          which leaves the (TEXT_LENGTH) for in-render calculation.
    *
    * Example: Item Count HUD, since Item Count HUD can be generalized into maximum item amount in inventory, which is usually 64 * 37 or 4 digits.
    *          we can just give the base hud width (ICON_WIDTH + GAP + TEXT_PADDING_LEFT + TEXT_LENGTH_OF_4_DIGITS + TEXT_PADDING_RIGHT).
    *          though yeah it will surely be a problem if the amount of item reaches over 5 digits.
    * */
    public abstract int getBaseHUDWidth();
    public abstract int getBaseHUDHeight();

    public void setHUDScale(DrawContext context) {
        float scaleFactor = getSettings().getScale() / (float) WINDOW.getScaleFactor();
        context.getMatrices().scale(scaleFactor, scaleFactor);
    }

    public void modifyXY() {
        int tempX = 0, tempY = 0;

        for (ConditionalSettings condition : baseHUDSettings.getConditions()) {
            if (condition.isConditionMet()) {
                tempX += condition.xOffset;
                tempY += condition.yOffset;
            }
        }

        x = baseX + tempX;
        y = baseY + tempY;
    }

    public void updateX() {
        baseX = getSettings().getCalculatedPosX() - getSettings().getGrowthDirectionX().getGrowthDirection(getBaseHUDWidth());
    }

    public void updateY() {
        baseY = getSettings().getCalculatedPosY() - getSettings().getGrowthDirectionY().getGrowthDirection(getBaseHUDHeight());
    }

    public boolean isScaled() {
        return this.getSettings().getScale() != 0 || (this.getSettings().getScale() / (double) WINDOW.getScaleFactor()) == 1;
    }

    public BaseHUDSettings getSettings() {
        return baseHUDSettings;
    }

    // bounding box attribute will return 0 if HUD is not rendered once.
    // the HUD must be rendered at least once to update the bounding box.

    public int getRawX() {
        return getBoundingBox().getX();
    }

    public int getRawY() {
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
