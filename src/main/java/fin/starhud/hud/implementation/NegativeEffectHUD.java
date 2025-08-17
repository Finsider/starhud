package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.EffectSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

public class NegativeEffectHUD extends AbstractEffectHUD {

    private static final EffectSettings SETTINGS = Main.settings.effectSettings.negativeSettings;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isRendered() {
        if (!SETTINGS.base.shouldRender()) return false;

        return !CLIENT.player.getStatusEffects().isEmpty();
    }

    public NegativeEffectHUD() {
        super(SETTINGS);
    }

    @Override
    public boolean isEffectAllowedToRender(RegistryEntry<StatusEffect> registryEntry) {
        return !registryEntry.value().isBeneficial();
    }

    @Override
    public String getName() {
        return "Negative Effect HUD";
    }

    @Override
    public String getId() {
        return HUDId.NEGATIVE_EFFECT.toString();
    }
}
