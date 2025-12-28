package fin.starhud.mixin;

import fin.starhud.Main;
import fin.starhud.condition.HeldItemTooltip;
import fin.starhud.hud.HUDComponent;
import fin.starhud.hud.HUDId;
import fin.starhud.hud.implementation.statuseffect.NegativeEffectHUD;
import fin.starhud.hud.implementation.statuseffect.PositiveEffectHUD;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, priority = 500)
public class MixinInGameHUD {
    @Unique
    private static final PositiveEffectHUD POSITIVE_EFFECT_HUD = (PositiveEffectHUD) HUDComponent.getInstance().getHUD(HUDId.POSITIVE_EFFECT);

    @Unique
    private static final NegativeEffectHUD NEGATIVE_EFFECT_HUD = (NegativeEffectHUD) HUDComponent.getInstance().getHUD(HUDId.NEGATIVE_EFFECT);

    // Mixin used to override vanilla effect HUD, I'm not sure whether this can be done using HUDElementRegistry
    @Inject(at = @At("HEAD"), method = "renderStatusEffectOverlay", cancellable = true)
    private void renderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if ((!Main.settings.generalSettings.inGameSettings.disableHUDRendering) && (POSITIVE_EFFECT_HUD.shouldRender() || NEGATIVE_EFFECT_HUD.shouldRender())) ci.cancel();
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
