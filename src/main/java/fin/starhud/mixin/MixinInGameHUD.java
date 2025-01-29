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
        if (Main.settings.handSettings.leftHandSettings.base.shouldRender) hand.renderLeftHandHUD(context);
        if (Main.settings.handSettings.rightHandSettings.base.shouldRender) hand.renderRightHandHUD(context);
        if (Main.settings.armorSettings.base.shouldRender) armor.renderArmorHUD(context);
        if (Main.settings.coordSettings.base.shouldRender) coordinate.renderCoordinateHUD(context);
        if (Main.settings.fpsSettings.base.shouldRender) fps.renderFPSHUD(context);
        if (Main.settings.pingSettings.base.shouldRender) ping.renderPingHUD(context);
        if (Main.settings.clockSettings.inGameSettings.base.shouldRender) clock.renderInGameTimeHUD(context);
        if (Main.settings.clockSettings.systemSettings.base.shouldRender) clock.renderSystemTimeHUD(context);
        if (Main.settings.directionSettings.base.shouldRender) direction.renderDirectionHUD(context);
        if (Main.settings.biomeSettings.base.shouldRender) biome.renderBiomeIndicatorHUD(context);
        if (Main.settings.inventorySettings.base.shouldRender) inventory.renderInventoryHUD(context);
    }
}
