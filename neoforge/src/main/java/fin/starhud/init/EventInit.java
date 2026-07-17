package fin.starhud.init;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.Settings;
import fin.starhud.helper.AttackTracker;
import fin.starhud.hud.HUDComponent;
import fin.starhud.screen.EditHUDScreen;
import me.shedaniel.autoconfig.AutoConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

public class EventInit {

    private static final GeneralSettings.InGameHUDSettings SETTINGS = Main.settings.generalSettings.inGameSettings;

    public static void init() {

        // register AttackTracker events, for combo and reach.
        NeoForge.EVENT_BUS.addListener(EventInit::onAttack);
        NeoForge.EVENT_BUS.addListener(EventInit::onAttackEndTick);

        // register keybinding event, on openEditHUDKey pressed -> move screen to edit hud screen.
        NeoForge.EVENT_BUS.addListener(EventInit::onOpenEditHUDKeyPressed);
        NeoForge.EVENT_BUS.addListener(EventInit::onToggleHUDKeyPressed);

        NeoForge.EVENT_BUS.addListener(EventInit::onClientStopping);

        // register hud element into before hotbar. I hope this was safe enough.
        NeoForge.EVENT_BUS.addListener(EventInit::onHUDRender);

        // extra on login to wait until Minecraft.instace() is valid
        NeoForge.EVENT_BUS.addListener(EventInit::onLogin);
    }

    public static void onClientStopping(GameShuttingDownEvent event) {
        AutoConfig.getConfigHolder(Settings.class).save();
    }

    public static void onOpenEditHUDKeyPressed(ClientTickEvent.Post event) {

        Minecraft client = Minecraft.getInstance();

        while (Main.openEditHUDKey.consumeClick()) {
            client.setScreenAndShow(new EditHUDScreen(Component.nullToEmpty("Edit HUD"), client.gui.screen()));
        }
    }

    public static void onToggleHUDKeyPressed(ClientTickEvent.Post event) {

        while (Main.toggleHUDKey.consumeClick()) {
            Main.settings.generalSettings.inGameSettings.disableHUDRendering = !Main.settings.generalSettings.inGameSettings.disableHUDRendering;
        }
    }

    public static void onHUDRender(RenderGuiEvent.Post event) {

        if (SETTINGS.disableHUDRendering) return;
        if (Minecraft.getInstance().gui.hud.isHidden()) return;
        if (Minecraft.getInstance().gui.screen() instanceof EditHUDScreen) return;

        HUDComponent.getInstance().collectAll();
        HUDComponent.getInstance().renderAll(event.getGuiGraphics());
    }


    public static void onAttack(AttackEntityEvent event) {
        AttackTracker.onAttack(event.getEntity(), event.getEntity().level(), event.getTarget());
    }

    public static void onAttackEndTick(ClientTickEvent.Post event) {
        AttackTracker.onEndTick(Minecraft.getInstance());
    }

    public static void onLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        HUDComponent.getInstance().init();
    }
}
