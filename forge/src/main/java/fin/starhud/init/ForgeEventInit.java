package fin.starhud.init;

import fin.starhud.config.Settings;
import fin.starhud.helper.AttackTracker;
import fin.starhud.hud.HUDComponent;
import me.shedaniel.autoconfig.AutoConfig;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class ForgeEventInit {

    public static void init() {

        // register AttackTracker events, for combo and reach.
        MinecraftForge.EVENT_BUS.addListener(ForgeEventInit::onAttack);
        MinecraftForge.EVENT_BUS.addListener(ForgeEventInit::onAttackEndTick);

        // register keybinding event, on openEditHUDKey pressed -> move screen to edit hud screen.
        MinecraftForge.EVENT_BUS.addListener(ForgeEventInit::onOpenEditHUDKeyPressed);
        MinecraftForge.EVENT_BUS.addListener(ForgeEventInit::onToggleHUDKeyPressed);

        MinecraftForge.EVENT_BUS.addListener(ForgeEventInit::onClientStopping);

        // register hud element into before hotbar. I hope this was safe enough.
        MinecraftForge.EVENT_BUS.addListener(ForgeEventInit::onHUDRender);

        // extra on login to wait until Minecraft.instace() is valid
        MinecraftForge.EVENT_BUS.addListener(ForgeEventInit::onLogin);
    }

    public static void onClientStopping(GameShuttingDownEvent event) {
        AutoConfig.getConfigHolder(Settings.class).save();
    }

    public static void onOpenEditHUDKeyPressed(TickEvent.ClientTickEvent event) {
        EventInit.onOpenEditHUDKeyPressed(Minecraft.getInstance());
    }

    public static void onToggleHUDKeyPressed(TickEvent.ClientTickEvent event) {
        EventInit.onToggleHUDKeyPressed(Minecraft.getInstance());
    }

    public static void onHUDRender(RenderGuiEvent.Post event) {
        EventInit.onHUDRender(event.getGuiGraphics(), event.getPartialTick());
    }


    public static void onAttack(AttackEntityEvent event) {
        AttackTracker.onAttack(event.getEntity(), event.getEntity().level(), event.getTarget());
    }

    public static void onAttackEndTick(TickEvent.ClientTickEvent event) {
        AttackTracker.onEndTick(Minecraft.getInstance());
    }

    public static void onLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        HUDComponent.getInstance().init();
    }
}
