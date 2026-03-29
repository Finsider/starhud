package fin.starhud.hud.implementation.statuseffect;

import fin.starhud.Main;
import fin.starhud.config.hud.statuseffect.EffectSettings;
import fin.starhud.hud.HUDId;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

public class NegativeEffectHUD extends AbstractEffectHUD {

    private static final EffectSettings SETTINGS = Main.settings.effectSettings.negativeSettings;

    public NegativeEffectHUD() {
        super(SETTINGS);
    }

    @Override
    public boolean isEffectAllowedToRender(Holder<MobEffect> registryEntry) {
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
