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
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class EventInit {

    private static final GeneralSettings.InGameHUDSettings SETTINGS = Main.settings.generalSettings.inGameSettings;

    public static void init() {

        // register AttackTracker events, for combo and reach.
        AttackEntityCallback.EVENT.register(EventInit::onAttack);
        ClientTickEvents.END_CLIENT_TICK.register(AttackTracker::onEndTick);

        // register keybinding event, on openEditHUDKey pressed -> move screen to edit hud screen.
        ClientTickEvents.END_CLIENT_TICK.register(EventInit::onOpenEditHUDKeyPressed);
        ClientTickEvents.END_CLIENT_TICK.register(EventInit::onToggleHUDKeyPressed);

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> AutoConfig.getConfigHolder(Settings.class).save());

        // register hud element into before hotbar. I hope this was safe enough.
        HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR, Identifier.withDefaultNamespace("starhud"), EventInit::onHUDRender);
    }

    public static void onOpenEditHUDKeyPressed(Minecraft client) {
        while (Main.openEditHUDKey.consumeClick()) {
            client.setScreenAndShow(new EditHUDScreen(Component.nullToEmpty("Edit HUD"), client.gui.screen()));
        }
    }

    public static void onToggleHUDKeyPressed(Minecraft client) {
        while (Main.toggleHUDKey.consumeClick()) {
            Main.settings.generalSettings.inGameSettings.disableHUDRendering = !Main.settings.generalSettings.inGameSettings.disableHUDRendering;
        }
    }

    public static void onHUDRender(GuiGraphicsExtractor context, DeltaTracker tickCounter) {
        if (SETTINGS.disableHUDRendering) return;
        if (Minecraft.getInstance().gui.hud.isHidden()) return;
        if (Minecraft.getInstance().gui.screen() instanceof EditHUDScreen) return;

        HUDComponent.getInstance().collectAll();
        HUDComponent.getInstance().renderAll(context);
    }

    public static InteractionResult onAttack(Player player, Level world, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
        AttackTracker.onAttack(player, world, entity);
        return InteractionResult.PASS;
    }
}
