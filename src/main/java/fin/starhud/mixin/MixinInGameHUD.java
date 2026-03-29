package fin.starhud.mixin;

import fin.starhud.condition.HeldItemTooltip;
import fin.starhud.condition.ScoreboardHUD;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Gui.class, priority = 500)
public class MixinInGameHUD {

    @Redirect(
            method = "displayScoreboardSidebar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void captureScoreboardFill(GuiGraphicsExtractor instance, int x1, int y1, int x2, int y2, int color) {
        ScoreboardHUD.captureBoundingBox(x1, y1 - 9, x2, y2); // -9 due to the first fill call is for header, which has 9 additional offset
        instance.fill(x1, y1, x2 ,y2 , color);
    }

    @Redirect(
            method = "extractSelectedItemName",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;textWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)V"
            ),
            require = 0
    )
    private void captureTooltipBox(GuiGraphicsExtractor instance, Font textRenderer, Component text, int x, int y, int width, int color) {
        HeldItemTooltip.setBoundingBox(x, y, width, 2 + 9 + 2);
        instance.textWithBackdrop(textRenderer, text, x, y, width, color);
    }
}
