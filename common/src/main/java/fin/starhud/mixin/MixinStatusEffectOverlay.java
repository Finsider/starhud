package fin.starhud.mixin;

import fin.starhud.Main;
import fin.starhud.hud.HUDComponent;
import fin.starhud.hud.HUDId;
import fin.starhud.hud.implementation.statuseffect.NegativeEffectHUD;
import fin.starhud.hud.implementation.statuseffect.PositiveEffectHUD;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class MixinStatusEffectOverlay {

    // Mixin used to override vanilla effect HUD, I'm not sure whether this can be done using HUDElementRegistry
    @Inject(at = @At("HEAD"), method = "extractEffects", cancellable = true)
    private void renderMobEffectOverlay(GuiGraphicsExtractor context, DeltaTracker tickCounter, CallbackInfo ci) {
        final PositiveEffectHUD POSITIVE_EFFECT_HUD = (PositiveEffectHUD) HUDComponent.getInstance().getHUD(HUDId.POSITIVE_EFFECT);
        final NegativeEffectHUD NEGATIVE_EFFECT_HUD = (NegativeEffectHUD) HUDComponent.getInstance().getHUD(HUDId.NEGATIVE_EFFECT);

        if (POSITIVE_EFFECT_HUD == null || NEGATIVE_EFFECT_HUD == null) return;

        if ((!Main.settings.generalSettings.inGameSettings.disableHUDRendering) && (POSITIVE_EFFECT_HUD.shouldRender() || NEGATIVE_EFFECT_HUD.shouldRender())) ci.cancel();
    }
}
