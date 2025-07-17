package fin.starhud.config;

import fin.starhud.helper.GrowthDirectionX;
import fin.starhud.helper.GrowthDirectionY;
import fin.starhud.helper.ScreenAlignmentX;
import fin.starhud.helper.ScreenAlignmentY;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class BaseHUDSettings implements ConfigData {
    @Comment("Toggle HUD")
    public boolean shouldRender;

    public int x;
    public int y;

    @Comment("HUD default Horizontal location")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ScreenAlignmentX originX;

    @Comment("HUD default Vertical location")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public ScreenAlignmentY originY;

    @Comment("Which way should the HUD goes when a the texture increases? (Recommended to go the opposite way from Alignment)")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public GrowthDirectionX growthDirectionX;

    @Comment("Which way should the HUD goes when a the texture increases? (Recommended to go the opposite way from Alignment)")
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public GrowthDirectionY growthDirectionY;

    @ConfigEntry.BoundedDiscrete(max = 6)
    @Comment("Set to 0 for default GUI Scale")
    public int scale = 0;

    public List<ConditionalSettings> conditions = new ArrayList<>();

    public BaseHUDSettings(boolean shouldRender, int x, int y, ScreenAlignmentX originX, ScreenAlignmentY originY, GrowthDirectionX growthDirectionX, GrowthDirectionY growthDirectionY) {
        this.shouldRender = shouldRender;
        this.x = x;
        this.y = y;
        this.originX = originX;
        this.originY = originY;
        this.growthDirectionX = growthDirectionX;
        this.growthDirectionY = growthDirectionY;
    }

    public boolean shouldRender() {
        if (!shouldRender) return false;

        for (ConditionalSettings condition : conditions)
            if (!condition.shouldRender())
                return false;

        return true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public GrowthDirectionX getGrowthDirectionX() {
        if (growthDirectionX == null) {
            if (getX() > 0) {
                growthDirectionX = GrowthDirectionX.RIGHT;
            } else if (getX() < 0) {
                growthDirectionX = GrowthDirectionX.LEFT;
            } else {
                growthDirectionX = GrowthDirectionX.CENTER;
            }
        }
        return growthDirectionX;
    }

    public GrowthDirectionY getGrowthDirectionY() {
        if (growthDirectionY == null) {
            if (getY() > 0) {
                growthDirectionY = GrowthDirectionY.DOWN;
            } else if (getY() < 0) {
                growthDirectionY = GrowthDirectionY.UP;
            } else {
                growthDirectionY = GrowthDirectionY.MIDDLE;
            }
        }
        return growthDirectionY;
    }

    public ScreenAlignmentX getOriginX() {
        if (originX == null) {
            if (getX() > 0) {
                originX = ScreenAlignmentX.LEFT;
            } else if (getX() < 0) {
                originX = ScreenAlignmentX.RIGHT;
            } else {
                originX = ScreenAlignmentX.CENTER;
            }
        }
        return originX;
    }

    public ScreenAlignmentY getOriginY() {
        if (originY == null) {
            if (getY() > 0) {
                originY = ScreenAlignmentY.TOP;
            } else if (getY() < 0) {
                originY = ScreenAlignmentY.BOTTOM;
            } else {
                originY = ScreenAlignmentY.MIDDLE;
            }
        }
        return originY;
    }

    public int getScale() {
        return scale;
    }

    public List<ConditionalSettings> getConditions() {
        return conditions;
    }

    // get the scaled factor
    // this can either make your HUD bigger or smaller.
    public float getScaledFactor() {
        return this.getScale() == 0 ? 1 : (float) MinecraftClient.getInstance().getWindow().getScaleFactor() / this.getScale();
    }

    // this shifts your HUD based on your x point, and alignment on X axis, and place them accordingly in your screen.
    public int getCalculatedPosX() {
        return this.getX() + (int) (this.getOriginX().getAlignmentPos(MinecraftClient.getInstance().getWindow().getScaledWidth()) * getScaledFactor());
    }

    // this also shifts your HUD based on your y point, and alignment on Y axis, and place them accordingly in your screen.
    public int getCalculatedPosY() {
        return this.getY() + (int) (this.getOriginY().getAlignmentPos(MinecraftClient.getInstance().getWindow().getScaledHeight()) * getScaledFactor());
    }
}
