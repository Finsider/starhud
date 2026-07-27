package fin.starhud.mixin;

import fin.starhud.condition.HeldItemTooltip;
import fin.starhud.condition.ScoreboardHUD;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Gui.class, priority = 500)
public class MixinInGameHUD {

    @Redirect(
            method = "displayScoreboardSidebar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void captureScoreboardFill(GuiGraphics instance, int minX, int minY, int maxX, int maxY, int color) {
        ScoreboardHUD.captureBoundingBox(minX, minY, maxX, maxY);
        instance.fill(minX, minY, maxX, maxY, color);
    }

    @Redirect(
            method = "renderSelectedItemName",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"
            ),
            require = 0
    )
    private void captureTooltipBox(GuiGraphics instance, int minX, int minY, int maxX, int maxY, int color) {
        HeldItemTooltip.setBoundingBox(minX, minY, maxX - minX, maxY - minY);

        instance.fill(minX, minY, maxX, maxY    , color);
    }
}
