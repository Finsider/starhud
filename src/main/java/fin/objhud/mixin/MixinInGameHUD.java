package fin.objhud.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import fin.objhud.hud.armor;
import fin.objhud.hud.coordinate;
import fin.objhud.hud.fps;
import fin.objhud.hud.ping;
import fin.objhud.hud.clock;

@Mixin(InGameHud.class)
public class MixinInGameHUD {

    @Inject(at = @At("TAIL"), method = "renderHotbar")
    private void renderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        coordinate.renderCoordinateHUD(context);
        fps.renderFPSHUD(context);
        ping.renderPingHUD(context);
        clock.renderInGameTimeHUD(context);
        clock.renderSystemTimeHUD(context);
        armor.renderArmorHUD(context);
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        armor.renderArmorDurabilityBar(context);
    }

}
