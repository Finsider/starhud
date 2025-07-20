package fin.starhud.mixin;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.hud.HUDComponent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHUD {

    @Unique
    private static final GeneralSettings.InGameHUDSettings SETTINGS = Main.settings.generalSettings.inGameSettings;

    // Mixin used to override vanilla effect HUD, I'm not sure whether this can be done using HUDElementRegistry
    @Inject(at = @At("HEAD"), method = "renderStatusEffectOverlay", cancellable = true)
    private void renderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (SETTINGS.disableHUDRendering) return;

        if (HUDComponent.getInstance().effectHUD.shouldRender()) {
            if (HUDComponent.getInstance().shouldRenderInGameScreen())
                HUDComponent.getInstance().effectHUD.render(context);
            ci.cancel();
        }
    }
}
