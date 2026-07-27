package fin.starhud.mixin;

import fin.starhud.condition.HeldItemTooltip;
import fin.starhud.condition.ScoreboardHUD;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Gui.class, priority = 500)
public class MixinInGameHUD {

    @Redirect(
            method = "renderSelectedItemName",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawStringWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)I"
            ),
            require = 0
    )
    private int captureTooltipBox(GuiGraphics context, Font textRenderer, Component text, int x, int y, int width, int color) {
        HeldItemTooltip.setBoundingBox(x, y, width, 2 + 9 + 2);
        context.drawStringWithBackdrop(textRenderer, text, x, y, width, color);
        return x;
    }
}
