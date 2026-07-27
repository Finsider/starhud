package fin.starhud.init;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.Settings;
import fin.starhud.helper.AttackTracker;
import fin.starhud.hud.HUDComponent;
import fin.starhud.screen.EditHUDScreen;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class FabricEventInit {

    public static void init() {

        // register AttackTracker events, for combo and reach.
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            AttackTracker.onAttack(player, world, entity);
            return InteractionResult.PASS;
        });

        ClientTickEvents.END_CLIENT_TICK.register(AttackTracker::onEndTick);

        // register keybinding event, on openEditHUDKey pressed -> move screen to edit hud screen.
        ClientTickEvents.END_CLIENT_TICK.register(EventInit::onOpenEditHUDKeyPressed);
        ClientTickEvents.END_CLIENT_TICK.register(EventInit::onToggleHUDKeyPressed);

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> AutoConfig.getConfigHolder(Settings.class).save());

        // register hud element into before hotbar. I hope this was safe enough.
        HudRenderCallback.EVENT.register(EventInit::onHUDRender);
    }


}
