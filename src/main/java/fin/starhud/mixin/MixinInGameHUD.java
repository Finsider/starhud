package fin.starhud.mixin;

import fin.starhud.Main;
import fin.starhud.hud.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHUD {

    @Inject(at = @At("TAIL"), method = "renderHotbar")
    private void renderHotbar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (Main.settings.handSettings.leftHandSettings.shouldRender) hand.renderLeftHandHUD(context);
        if (Main.settings.handSettings.rightHandSettings.shouldRender) hand.renderRightHandHUD(context);
        if (Main.settings.armorSettings.shouldRender) armor.renderArmorHUD(context);
        if (Main.settings.coordSettings.shouldRender) coordinate.renderCoordinateHUD(context);
        if (Main.settings.fpsSettings.shouldRender) fps.renderFPSHUD(context);
        if (Main.settings.pingSettings.shouldRender) ping.renderPingHUD(context);
        if (Main.settings.clockSettings.inGameSettings.shouldRender) clock.renderInGameTimeHUD(context);
        if (Main.settings.clockSettings.systemSettings.shouldRender) clock.renderSystemTimeHUD(context);
        if (Main.settings.directionSettings.shouldRender) direction.renderDirectionHUD(context);
        if (Main.settings.biomeSettings.shouldRender) biome.renderBiomeIndicatorHUD(context);
        if (Main.settings.inventorySettings.shouldRender) inventory.renderInventoryHUD(context);
    }
}
