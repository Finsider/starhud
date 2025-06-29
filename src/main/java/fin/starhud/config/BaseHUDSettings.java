package fin.starhud.config;

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

    @ConfigEntry.BoundedDiscrete(max = 6)
    @Comment("Set to 0 for default GUI Scale")
    public int scale = 0;

    public List<ConditionalSettings> conditions = new ArrayList<>();

    public BaseHUDSettings(boolean shouldRender, int x, int y, ScreenAlignmentX originX, ScreenAlignmentY originY) {
        this.shouldRender = shouldRender;
        this.x = x;
        this.y = y;
        this.originX = originX;
        this.originY = originY;
    }

    // get the scaled factor
    // this can either make your HUD bigger or smaller.
    public float getScaledFactor() {
        return this.scale == 0 ? 1 : (float) MinecraftClient.getInstance().getWindow().getScaleFactor() / this.scale;
    }

    // this shifts your HUD based on your x point, and alignment on X axis, and place them accordingly in your screen.
    public int getCalculatedPosX(int HUDWidth) {
        return this.x + (int) (this.originX.getAlignmentPos(MinecraftClient.getInstance().getWindow().getScaledWidth()) * getScaledFactor()) - this.originX.getTextureOffset(HUDWidth);
    }

    // this also shifts your HUD based on your y point, and alignment on Y axis, and place them accordingly in your screen.
    public int getCalculatedPosY(int HUDHeight) {
        return this.y + (int) (this.originY.getAlignmentPos(MinecraftClient.getInstance().getWindow().getScaledHeight()) * getScaledFactor()) - this.originY.getTextureOffset(HUDHeight);
    }
}
