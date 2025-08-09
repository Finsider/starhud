package fin.starhud.hud.implementation;

import fin.starhud.Main;
import fin.starhud.config.hud.EffectSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

public class PositiveEffectHUD extends AbstractEffectHUD {

    private static final EffectSettings SETTINGS = Main.settings.effectSettings.positiveSettings;

    public PositiveEffectHUD() {
        super(SETTINGS);
    }

    @Override
    public boolean isEffectAllowedToRender(StatusEffect registryEntry) {
        return registryEntry.isBeneficial();
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
