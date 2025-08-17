package fin.starhud.mixin;

import fin.starhud.Main;
import fin.starhud.helper.condition.HeldItemTooltip;
import fin.starhud.helper.condition.ScoreboardHUD;
import fin.starhud.hud.implementation.NegativeEffectHUD;
import fin.starhud.hud.implementation.PositiveEffectHUD;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, priority = 500)
public class MixinInGameHUD {


    @Inject(at = @At("HEAD"), method = "renderStatusEffectOverlay", cancellable = true)
    private void renderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if ((!Main.settings.generalSettings.inGameSettings.disableHUDRendering) && (PositiveEffectHUD.isRendered() || NegativeEffectHUD.isRendered())) ci.cancel();
    }

    @Redirect(
            method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void captureScoreboardFill(DrawContext instance, int x1, int y1, int x2, int y2, int color) {
        ScoreboardHUD.captureBoundingBox(x1, y1 - 9, x2, y2); // -9 due to the first fill call is for header, which has 9 additional offset
        instance.fill(x1, y1, x2 ,y2 , color);
    }

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
