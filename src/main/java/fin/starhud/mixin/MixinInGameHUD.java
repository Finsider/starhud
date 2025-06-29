package fin.starhud.mixin;

import fin.starhud.hud.HUDComponent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHUD {

    @Inject(at = @At("TAIL"), method = "renderHotbar")
    private void renderHotbar(float tickDelta, DrawContext context, CallbackInfo ci) {
        HUDComponent.getInstance().renderAll(context);
    }

    @Inject(at = @At("HEAD"), method = "renderStatusEffectOverlay", cancellable = true)
    private void renderStatusEffectOverlay(DrawContext context, CallbackInfo ci) {
        if (HUDComponent.getInstance().effectHUD.shouldRender()) {
            HUDComponent.getInstance().effectHUD.render(context);
            ci.cancel();
        }
    }
}
