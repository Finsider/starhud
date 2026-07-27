package fin.starhud.init;

import fin.starhud.config.Settings;
import fin.starhud.helper.AttackTracker;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.world.InteractionResult;

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
