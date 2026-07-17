package fin.starhud.hud.implementation.statuseffect;

import fin.starhud.Main;
import fin.starhud.config.hud.statuseffect.EffectSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

public class PositiveEffectHUD extends AbstractEffectHUD {

    private static final EffectSettings SETTINGS = Main.settings.effectSettings.positiveSettings;

    public PositiveEffectHUD() {
        super(SETTINGS);
    }

    @Override
    public boolean isEffectAllowedToRender(Holder<MobEffect> registryEntry) {
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
