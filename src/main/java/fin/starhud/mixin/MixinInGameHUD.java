package fin.starhud.mixin;

import fin.starhud.condition.HeldItemTooltip;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = InGameHud.class, priority = 500)
public class MixinInGameHUD {

    @Redirect(
            method = "renderHeldItemTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I"
            ),
            require = 0
    )
    private int captureHeldItemTooltipBox(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int width, int color) {
        HeldItemTooltip.setBoundingBox(x, y, width, 2 + 9 + 2);
        instance.drawTextWithBackground(textRenderer, text, x, y, width, color);
        return x;
    }
}
