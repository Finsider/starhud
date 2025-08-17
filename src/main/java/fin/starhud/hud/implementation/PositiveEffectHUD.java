package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.EffectSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

public class PositiveEffectHUD extends AbstractEffectHUD {

    private static final EffectSettings SETTINGS = Main.settings.effectSettings.positiveSettings;
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    public static boolean isRendered() {
        if (!SETTINGS.base.shouldRender()) return false;

        return !CLIENT.player.getStatusEffects().isEmpty();
    }

    public PositiveEffectHUD() {
        super(SETTINGS);
    }

    @Override
    public boolean isEffectAllowedToRender(RegistryEntry<StatusEffect> registryEntry) {
        return registryEntry.value().isBeneficial();
    }

    @Override
    public String getName() {
        return "Positive Effect HUD";
    }

    @Override
    public String getId() {
        return HUDId.POSITIVE_EFFECT.toString();
    }
}
