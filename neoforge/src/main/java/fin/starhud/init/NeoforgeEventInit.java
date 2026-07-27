package fin.starhud.init;

import fin.starhud.Main;
import fin.starhud.config.GeneralSettings;
import fin.starhud.config.Settings;
import fin.starhud.helper.AttackTracker;
import fin.starhud.hud.HUDComponent;
import fin.starhud.screen.EditHUDScreen;
import me.shedaniel.autoconfig.AutoConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

public class NeoforgeEventInit {

    private static final GeneralSettings.InGameHUDSettings SETTINGS = Main.settings.generalSettings.inGameSettings;

    public static void init() {

        // register AttackTracker events, for combo and reach.
        NeoForge.EVENT_BUS.addListener(NeoforgeEventInit::onAttack);
        NeoForge.EVENT_BUS.addListener(NeoforgeEventInit::onAttackEndTick);

        // register keybinding event, on openEditHUDKey pressed -> move screen to edit hud screen.
        NeoForge.EVENT_BUS.addListener(NeoforgeEventInit::onOpenEditHUDKeyPressed);
        NeoForge.EVENT_BUS.addListener(NeoforgeEventInit::onToggleHUDKeyPressed);

        NeoForge.EVENT_BUS.addListener(NeoforgeEventInit::onClientStopping);

        // register hud element into before hotbar. I hope this was safe enough.
        NeoForge.EVENT_BUS.addListener(NeoforgeEventInit::onHUDRender);

        // extra on login to wait until Minecraft.instace() is valid
        NeoForge.EVENT_BUS.addListener(NeoforgeEventInit::onLogin);
    }

    public static void onClientStopping(GameShuttingDownEvent event) {
        AutoConfig.getConfigHolder(Settings.class).save();
    }

    public static void onOpenEditHUDKeyPressed(ClientTickEvent.Post event) {
        EventInit.onOpenEditHUDKeyPressed(Minecraft.getInstance());
    }

    public static void onToggleHUDKeyPressed(ClientTickEvent.Post event) {
        EventInit.onToggleHUDKeyPressed(Minecraft.getInstance());
    }

    public static void onHUDRender(RenderGuiEvent.Post event) {
        EventInit.onHUDRender(event.getGuiGraphics(), event.getPartialTick());
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
